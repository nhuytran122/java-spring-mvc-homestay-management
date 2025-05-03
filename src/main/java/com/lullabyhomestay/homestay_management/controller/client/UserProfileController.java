package com.lullabyhomestay.homestay_management.controller.client;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.lullabyhomestay.homestay_management.domain.dto.PasswordChangeDTO;
import com.lullabyhomestay.homestay_management.domain.dto.UserDTO;
import com.lullabyhomestay.homestay_management.service.BookingService;
import com.lullabyhomestay.homestay_management.service.UploadService;
import com.lullabyhomestay.homestay_management.service.UserService;
import com.lullabyhomestay.homestay_management.utils.AuthUtils;
import com.lullabyhomestay.homestay_management.utils.BookingStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@PreAuthorize("hasRole('CUSTOMER')")
public class UserProfileController {
        private final BookingService bookingService;
        private final UploadService uploadService;
        private final UserService userService;
        private final ModelMapper mapper;

        @GetMapping("/profile")
        public String getProfilePage(Model model) {
                UserDTO currentUserDTO = AuthUtils.getLoggedInUser(userService);
                Long customerID = currentUserDTO.getCustomer().getCustomerID();
                model.addAttribute("user", currentUserDTO);

                model.addAttribute("countTotalBooked",
                                bookingService.countTotalBookingByCustomerID(customerID));
                model.addAttribute("countCancelled",
                                bookingService.countByBookingStatusAndCustomerID(BookingStatus.CANCELLED, customerID));
                model.addAttribute("countCompleted",
                                bookingService.countByBookingStatusAndCustomerID(BookingStatus.COMPLETED, customerID));
                model.addAttribute("paidTotal", bookingService.getTotalAmountByCustomerID(customerID));
                return "client/user/profile";
        }

        @GetMapping("/profile/update")
        public String getUpdateProfilePage(Model model) {
                UserDTO currentUserDTO = AuthUtils.getLoggedInUser(userService);
                model.addAttribute("user", currentUserDTO);
                return "client/user/update";
        }

        @PostMapping("/profile/update")
        public String postUpdateProfile(@ModelAttribute("user") @Valid UserDTO userDTO,
                        BindingResult result,
                        @RequestParam("fileImg") MultipartFile file,
                        HttpServletRequest request) {
                HttpSession session = request.getSession(false);
                Long userID = AuthUtils.getLoggedInUserID(session);
                if (result.hasErrors())
                        return "client/user/update";
                String img;
                if (!file.isEmpty()) {
                        img = this.uploadService.handleSaveUploadFile(file, "avatar");
                        userDTO.setAvatar(img);
                }
                userDTO = userService.updateProfile(userID, userDTO);
                AuthUtils.handleUpdateUserSession(userDTO, session);
                return "redirect:/profile";
        }

        @GetMapping("/change-password")
        public String getChangePasswordPage(Model model) {
                AuthUtils.getLoggedInCustomer(userService, mapper);
                model.addAttribute("passwordForm", new PasswordChangeDTO());
                return "client/auth/change-password";
        }

        @PostMapping("/change-password")
        public String changePassword(@ModelAttribute("passwordForm") @Valid PasswordChangeDTO passwordForm,
                        BindingResult result,
                        HttpSession session,
                        Model model) {
                Long userID = AuthUtils.getLoggedInUserID(session);
                if (result.hasErrors()) {
                        return "client/auth/change-password";
                }
                userService.changePassword(userID, passwordForm.getNewPassword());
                model.addAttribute("message", "Đổi mật khẩu thành công! Bạn sẽ được chuyển hướng sau 3 giây.");
                model.addAttribute("redirect", "/profile");

                return "client/auth/change-password";
        }
}
