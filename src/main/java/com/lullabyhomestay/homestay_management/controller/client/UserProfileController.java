package com.lullabyhomestay.homestay_management.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.lullabyhomestay.homestay_management.domain.dto.CustomerDTO;
import com.lullabyhomestay.homestay_management.domain.dto.PasswordChangeDTO;
import com.lullabyhomestay.homestay_management.service.BookingService;
import com.lullabyhomestay.homestay_management.service.CustomerService;
import com.lullabyhomestay.homestay_management.service.UploadService;
import com.lullabyhomestay.homestay_management.utils.AuthUtils;
import com.lullabyhomestay.homestay_management.utils.BookingStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class UserProfileController {
        private final CustomerService customerService;
        private final BookingService bookingService;
        private final UploadService uploadService;

        @GetMapping("/profile")
        public String getProfilePage(Model model) {
                CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(customerService);
                Long customerID = customerDTO.getCustomerID();
                model.addAttribute("customer", customerDTO);

                model.addAttribute("countBooked",
                                bookingService.countByBookingStatusAndCustomerID(BookingStatus.BOOKED, customerID));
                model.addAttribute("countCancelled",
                                bookingService.countByBookingStatusAndCustomerID(BookingStatus.CANCELLED, customerID));
                model.addAttribute("countCompleted",
                                bookingService.countByBookingStatusAndCustomerID(BookingStatus.COMPLETED, customerID));
                model.addAttribute("paidTotal", bookingService.getTotalAmountByCustomerID(customerID));
                return "client/user/profile";
        }

        @GetMapping("/profile/update")
        public String getUpdateProfilePage(Model model) {
                CustomerDTO currentCustomerDTO = AuthUtils.getLoggedInCustomer(customerService);

                model.addAttribute("customer", currentCustomerDTO);
                return "client/user/update";
        }

        @PostMapping("/profile/update")
        public String postUpdateProfile(@ModelAttribute("customer") @Valid CustomerDTO customerDTO,
                        BindingResult result,
                        @RequestParam("fileImg") MultipartFile file,
                        HttpServletRequest request) {
                HttpSession session = request.getSession(false);
                CustomerDTO currentCustomerDTO = AuthUtils.getLoggedInCustomer(customerService);
                if (result.hasErrors())
                        return "client/user/update";
                String img;
                if (!file.isEmpty()) {
                        img = this.uploadService.handleSaveUploadFile(file, "avatar");
                        currentCustomerDTO.setAvatar(img);
                }
                currentCustomerDTO.setFullName(customerDTO.getFullName());
                currentCustomerDTO.setPhone(customerDTO.getPhone());
                currentCustomerDTO.setAddress(customerDTO.getAddress());

                customerService.handleSaveCustomer(currentCustomerDTO);
                AuthUtils.handleUpdateUserSession(currentCustomerDTO, session);
                return "redirect:/profile";
        }

        @GetMapping("/change-password")
        public String getChangePasswordPage(Model model) {
                CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(customerService);
                model.addAttribute("customer", customerDTO);
                model.addAttribute("passwordForm", new PasswordChangeDTO());
                return "client/auth/change-password";
        }

        @PostMapping("/change-password")
        public String changePassword(@ModelAttribute("passwordForm") @Valid PasswordChangeDTO passwordForm,
                        BindingResult result,
                        HttpSession session,
                        Model model) {
                CustomerDTO customerDTO = AuthUtils.getLoggedInCustomer(customerService);
                if (result.hasErrors()) {
                        return "client/auth/change-password";
                }
                customerService.changePassword(customerDTO.getCustomerID(), passwordForm.getNewPassword());
                model.addAttribute("message", "Đổi mật khẩu thành công! Bạn sẽ được chuyển hướng sau 3 giây.");
                model.addAttribute("redirect", "/profile");

                return "client/auth/change-password";
        }
}
