package com.lullabyhomestay.homestay_management.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.PasswordResetToken;
import com.lullabyhomestay.homestay_management.domain.Role;
import com.lullabyhomestay.homestay_management.domain.User;
import com.lullabyhomestay.homestay_management.domain.dto.CustomerDTO;
import com.lullabyhomestay.homestay_management.domain.dto.UserDTO;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.PasswordResetTokenRepository;
import com.lullabyhomestay.homestay_management.repository.RoleRepository;
import com.lullabyhomestay.homestay_management.repository.UserRepository;
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

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByUserID(Long id) {
        return userRepository.findByUserID(id)
                .orElseThrow(() -> new NotFoundException("Nguời dùng"));
    }

    public UserDTO getUserDTOByEmail(String email) {
        return mapper.map(getUserByEmail(email), CustomerDTO.class);
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
        return handleSaveUser(user);
    }

    public UserDTO updateProfile(Long id, UserDTO requestDTO) {
        User user = getUserByUserID(id);
        updateUserInfoFromDTO(user, requestDTO);
        return mapper.map(handleSaveUser(user), UserDTO.class);
    }

    public void changePassword(Long userID, String newPassword) {
        User user = getUserByUserID(userID);
        user.setPassword(passwordEncoder.encode(newPassword));
        handleSaveUser(user);
    }

    public User handleSaveUser(User user) {
        return userRepository.save(user);
    }

    private String encodePasswordOrDefault(String password) {
        return passwordEncoder.encode(password != null ? password : "lullabyhomestay");
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
    }
}
