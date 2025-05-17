package com.lullabyhomestay.homestay_management.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.Booking;
import com.lullabyhomestay.homestay_management.domain.User;
import com.lullabyhomestay.homestay_management.utils.ConvertDateToString;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmailService {

        private final JavaMailSender mailSender;
        private final ResourceLoader resourceLoader;

        @Value("${app.baseURL}")
        private String baseUrl;

        @Value("${app.sendFrom}")
        private String sendFrom;

        @Async
        public void sendCheckInReminderEmail(Booking booking) {
                try {
                        NumberFormat currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                        String template = loadTemplate("/template-emails/reminder-email.html");

                        Map<String, String> placeholders = new HashMap<>();
                        placeholders.put("bookingId", String.valueOf(booking.getBookingID()));
                        placeholders.put("customerName", booking.getCustomer().getUser().getFullName());
                        placeholders.put("createdAt", booking.getCreatedAt() != null
                                        ? booking.getCreatedAt().format(dateFormatter)
                                        : "N/A");
                        placeholders.put("checkIn", booking.getCheckIn().format(dateFormatter));
                        placeholders.put("checkOut", booking.getCheckOut().format(dateFormatter));
                        placeholders.put("guestCount", String.valueOf(booking.getGuestCount()));
                        placeholders.put("totalAmount", currencyFormat
                                        .format(booking.getTotalAmount() != null ? booking.getTotalAmount() : 0));
                        placeholders.put("paidAmount", currencyFormat
                                        .format(booking.getPaidAmount() != null ? booking.getPaidAmount() : 0));
                        placeholders.put("roomNumber", String.valueOf(booking.getRoom().getRoomNumber()));
                        placeholders.put("roomType", booking.getRoom().getRoomType().getName());
                        placeholders.put("branchName", booking.getRoom().getBranch().getBranchName());
                        placeholders.put("branchAddress", booking.getRoom().getBranch().getAddress());
                        placeholders.put("branchPhone", booking.getRoom().getBranch().getPhone());
                        placeholders.put("branchPassword", booking.getRoom().getBranch().getGatePassword());
                        placeholders.put("bookingHistoryLink",
                                        baseUrl + "/booking/booking-history/" + booking.getBookingID());

                        String content = renderTemplate(template, placeholders);

                        sendHtmlEmail(
                                        booking.getCustomer().getUser().getEmail(),
                                        "Nhắc nhở check-in & mật khẩu cổng - Đơn đặt phòng lúc " +
                                                        ConvertDateToString.getFormattedDateTime(booking.getCheckIn()),
                                        content);

                } catch (Exception e) {
                        log.error("Gửi email nhắc nhở thất bại cho booking ID {}: {}", booking.getBookingID(),
                                        e.getMessage(), e);
                }
        }

        @Async
        public void sendPasswordResetEmail(User user, String token) throws MessagingException, IOException {
                String template = loadTemplate("/template-emails/reset-password-email.html");

                Map<String, String> placeholders = Map.of(
                                "userName", user.getFullName(),
                                "resetLink", baseUrl + "/reset-password?token=" + token);

                String content = renderTemplate(template, placeholders);

                sendHtmlEmail(user.getEmail(), "Yêu cầu đặt lại mật khẩu - Lullaby Homestay", content);
        }

        @Async
        public void sendEmailVerificationEmail(User user, String token) throws MessagingException, IOException {
                String template = loadTemplate("/template-emails/email-verification.html");

                Map<String, String> placeholders = Map.of(
                                "userName", user.getFullName() != null ? user.getFullName() : "Khách hàng",
                                "verifyLink", baseUrl + "/verify-email?token=" + token);

                String content = renderTemplate(template, placeholders);

                sendHtmlEmail(user.getEmail(), "Xác nhận Email Đăng ký - Lullaby Homestay", content);
        }

        @Async
        public void sendPasswordChangedNotification(User user) throws MessagingException, IOException {
                String template = loadTemplate("/template-emails/password-changed-notification.html");

                Map<String, String> placeholders = Map.of(
                                "userName", user.getFullName()

                );

                String content = renderTemplate(template, placeholders);

                sendHtmlEmail(user.getEmail(), "Thông báo thay đổi mật khẩu - Lullaby Homestay", content);
        }

        private String loadTemplate(String path) throws IOException {
                Resource resource = resourceLoader.getResource("classpath:" + path);
                if (!resource.exists()) {
                        throw new IOException("Không tìm thấy template: " + path);
                }
                return new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);
        }

        private String renderTemplate(String content, Map<String, String> placeholders) {
                for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                        content = content.replace("${" + entry.getKey() + "}", entry.getValue());
                }
                return content;
        }

        private void sendHtmlEmail(String to, String subject, String htmlContent)
                        throws MessagingException, IOException {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setTo(to);
                helper.setSubject(subject);
                helper.setFrom(sendFrom);
                helper.setText(htmlContent, true);

                attachLogo(helper);

                mailSender.send(message);
        }

        private void attachLogo(MimeMessageHelper helper) throws IOException, MessagingException {
                Resource logoResource = resourceLoader.getResource("classpath:/static/images/lullaby.jpg");
                helper.addInline("logo.jpg", logoResource.getFile());
        }
}
