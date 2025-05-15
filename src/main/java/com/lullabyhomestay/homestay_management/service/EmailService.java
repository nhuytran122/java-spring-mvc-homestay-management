package com.lullabyhomestay.homestay_management.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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
        public void sendCheckInReminderEmail(Booking booking) throws MessagingException, IOException {
                try {
                        MimeMessage message = mailSender.createMimeMessage();
                        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                        helper.setTo(booking.getCustomer().getUser().getEmail());
                        helper.setSubject("Nhắc nhở check-in & mật khẩu cổng - Đơn đặt phòng lúc " +
                                        ConvertDateToString.getFormattedDateTime(booking.getCheckIn()));
                        helper.setFrom(sendFrom);

                        String emailContent = buildCheckInReminderEmail(booking);
                        helper.setText(emailContent, true);

                        helper.addInline("logo.jpg",
                                        resourceLoader.getResource("classpath:/static/images/lullaby.jpg").getFile());

                        mailSender.send(message);
                } catch (Exception e) {
                        log.error("Gửi email nhắc nhở thất bại cho booking ID {}: {}", booking.getBookingID(),
                                        e.getMessage(), e);
                        System.out.println(e.getMessage());
                }
        }

        private String buildCheckInReminderEmail(Booking booking) throws IOException {
                NumberFormat currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                Resource resource = resourceLoader.getResource("classpath:/template-emails/reminder-email.html");
                if (!resource.exists()) {
                        throw new IOException("Không tìm thấy template email");
                }
                String content = new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);

                content = content.replace("${bookingId}", String.valueOf(booking.getBookingID()))
                                .replace("${customerName}",
                                                booking.getCustomer() != null && booking.getCustomer().getUser() != null
                                                                ? booking.getCustomer().getUser().getFullName()
                                                                : "Khách hàng")
                                .replace("${createdAt}", booking.getCreatedAt() != null
                                                ? booking.getCreatedAt().format(dateFormatter)
                                                : "N/A")
                                .replace("${checkIn}", booking.getCheckIn().format(dateFormatter))
                                .replace("${checkOut}", booking.getCheckOut().format(dateFormatter))
                                .replace("${guestCount}", String.valueOf(booking.getGuestCount()))
                                .replace("${totalAmount}", currencyFormat.format(booking.getTotalAmount() != null
                                                ? booking.getTotalAmount()
                                                : 0))
                                .replace("${paidAmount}", currencyFormat.format(booking.getPaidAmount() != null
                                                ? booking.getPaidAmount()
                                                : 0))
                                .replace("${roomNumber}", booking.getRoom() != null
                                                ? String.valueOf(booking.getRoom().getRoomNumber())
                                                : "N/A")
                                .replace("${roomType}",
                                                booking.getRoom() != null && booking.getRoom().getRoomType() != null
                                                                ? booking.getRoom().getRoomType().getName()
                                                                : "N/A")
                                .replace("${branchName}",
                                                booking.getRoom() != null && booking.getRoom().getBranch() != null
                                                                ? booking.getRoom().getBranch().getBranchName()
                                                                : "N/A")
                                .replace("${branchAddress}",
                                                booking.getRoom() != null && booking.getRoom().getBranch() != null
                                                                ? booking.getRoom().getBranch().getAddress()
                                                                : "N/A")
                                .replace("${branchPhone}",
                                                booking.getRoom() != null && booking.getRoom().getBranch() != null
                                                                ? booking.getRoom().getBranch().getPhone()
                                                                : "N/A")
                                .replace("${branchPassword}",
                                                booking.getRoom() != null && booking.getRoom().getBranch() != null
                                                                ? booking.getRoom().getBranch().getBranchPassword()
                                                                : "N/A")
                                .replace("${bookingHistoryLink}",
                                                baseUrl + "/booking/booking-history/" + booking.getBookingID());

                return content;
        }

        public void sendPasswordResetEmail(User user, String token) throws MessagingException, IOException {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setTo(user.getEmail());
                helper.setSubject("Yêu cầu đặt lại mật khẩu - Lullaby Homestay");
                helper.setFrom(sendFrom);

                String emailContent = buildPasswordResetEmail(user, token);
                helper.setText(emailContent, true);

                helper.addInline("logo.jpg",
                                resourceLoader.getResource("classpath:/static/images/lullaby.jpg").getFile());
                try {
                        mailSender.send(message);
                        log.info("Gửi email thành công: {}", user.getEmail());
                } catch (Exception e) {
                        log.error("Gửi email thất bại {}: {}", user.getEmail(), e.getMessage(),
                                        e);
                        throw e;
                }
        }

        private String buildPasswordResetEmail(User user, String token) throws IOException {
                Resource resource = resourceLoader.getResource("classpath:/template-emails/reset-password-email.html");
                if (!resource.exists()) {
                        log.error("Không tìm thấy template:/template-emails/reset-password-email.html");
                        throw new IOException("Không tìm thấy template");
                }
                String content = new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);

                String resetLink = baseUrl + "/reset-password?token=" + token;
                content = content.replace("${userName}", user.getFullName())
                                .replace("${resetLink}", resetLink);

                return content;
        }
}