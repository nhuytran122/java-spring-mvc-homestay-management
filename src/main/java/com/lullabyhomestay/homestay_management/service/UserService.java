package com.lullabyhomestay.homestay_management.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.EmailVerificationToken;
import com.lullabyhomestay.homestay_management.domain.PasswordResetToken;
import com.lullabyhomestay.homestay_management.domain.Role;
import com.lullabyhomestay.homestay_management.domain.User;
import com.lullabyhomestay.homestay_management.domain.dto.RegisterDTO;
import com.lullabyhomestay.homestay_management.domain.dto.UserDTO;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.EmailVerificationTokenRepository;
import com.lullabyhomestay.homestay_management.repository.PasswordResetTokenRepository;
import com.lullabyhomestay.homestay_management.repository.RoleRepository;
import com.lullabyhomestay.homestay_management.repository.UserRepository;
import com.lullabyhomestay.homestay_management.utils.Constants;
import com.lullabyhomestay.homestay_management.utils.SystemRole;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;
    private final EmailService emailService;

    private final PasswordResetTokenRepository tokenRepository;
    private final EmailVerificationTokenRepository verificationTokenRepository;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByUserID(Long id) {
        return userRepository.findByUserID(id)
                .orElseThrow(() -> new NotFoundException("Nguời dùng"));
    }

    public UserDTO getUserDTOByEmail(String email) {
        return mapper.map(getUserByEmail(email), UserDTO.class);
    }

    public boolean checkEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public User createUserForPerson(UserDTO userDTO, Long roleID, String password) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(encodePasswordOrDefault(password));
        updateUserInfoFromDTO(user, userDTO);
        user.setRole(resolveRole(roleID));
        user.setIsEnabled(true);
        return handleSaveUser(user);
    }

    @Transactional
    public User registerUserWithVerification(RegisterDTO registerDTO) throws Exception {
        if (checkEmailExist(registerDTO.getEmail())) {
            throw new Exception("Email đã được sử dụng");
        }

        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setPassword(encodePasswordOrDefault(registerDTO.getPassword()));
        user.setIsEnabled(false);
        updateUserInfoFromRegisterDTO(user, registerDTO);
        user.setRole(resolveRole(null));
        User savedUser = handleSaveUser(user);

        createAndSendVerificationToken(savedUser);

        return savedUser;
    }

    private void updateUserInfoFromRegisterDTO(User user, RegisterDTO dto) {
        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
    }

    @Transactional
    private void createAndSendVerificationToken(User user) throws Exception {
        verificationTokenRepository.deleteByUser(user);
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = new EmailVerificationToken(
                token,
                user,
                LocalDateTime.now().plusHours(24));
        verificationTokenRepository.save(verificationToken);
        emailService.sendEmailVerificationEmail(user, token);
    }

    public UserDTO updateProfile(Long id, UserDTO requestDTO) {
        User user = getUserByUserID(id);
        updateUserInfoFromDTO(user, requestDTO);
        return mapper.map(handleSaveUser(user), UserDTO.class);
    }

    public void changePassword(Long userID, String newPassword) throws Exception {
        User user = getUserByUserID(userID);
        user.setPassword(passwordEncoder.encode(newPassword));
        handleSaveUser(user);
        emailService.sendPasswordChangedNotification(user);
    }

    public User handleSaveUser(User user) {
        return userRepository.save(user);
    }

    private String encodePasswordOrDefault(String password) {
        return passwordEncoder.encode(password != null ? password : Constants.DEFAULT_PASSWORD);
    }

    private Role resolveRole(Long roleID) {
        if (roleID != null) {
            return roleRepository.findById(roleID)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy vai trò với ID: " + roleID));
        }
        return roleRepository.findByRoleName(SystemRole.CUSTOMER);
    }

    private void updateUserInfoFromDTO(User user, UserDTO dto) {
        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        if (dto.getAvatar() != null) {
            user.setAvatar(dto.getAvatar());
        }
    }

    @Transactional
    public void requestPasswordReset(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("Email không tồn tại");
        }
        tokenRepository.deleteByUser(user);
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(
                token,
                user,
                LocalDateTime.now().plusMinutes(30));
        tokenRepository.save(resetToken);
        emailService.sendPasswordResetEmail(user, token);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) throws Exception {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null || resetToken.isExpired() || resetToken.getIsUsed()) {
            throw new Exception("Token không hợp lệ hoặc đã hết hạn");
        }
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setIsUsed(true);
        tokenRepository.save(resetToken);
        emailService.sendPasswordChangedNotification(user);
    }

    @Transactional
    public void verifyEmail(String token) throws Exception {
        EmailVerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            throw new Exception("Liên kết xác nhận không hợp lệ.");
        }

        if (verificationToken.isExpired()) {
            throw new Exception("Liên kết xác nhận đã hết hạn. Vui lòng yêu cầu gửi lại.");
        }

        if ((verificationToken.getIsUsed())) {
            throw new Exception("Liên kết xác nhận này đã được sử dụng.");
        }

        User user = verificationToken.getUser();
        if (user.getIsEnabled()) {
            throw new Exception("Tài khoản đã được xác nhận trước đó.");
        }

        user.setIsEnabled(true);
        userRepository.save(user);

        verificationToken.setIsUsed(true);
        verificationTokenRepository.save(verificationToken);
    }

    @Transactional
    public void resendVerificationEmail(String email) throws Exception {
        User user = getUserByEmail(email);
        if (user == null) {
            throw new Exception("Không tìm thấy người dùng với email này.");
        }
        if (user.getIsEnabled()) {
            throw new Exception("Tài khoản đã được xác nhận.");
        }
        createAndSendVerificationToken(user);
    }
}
