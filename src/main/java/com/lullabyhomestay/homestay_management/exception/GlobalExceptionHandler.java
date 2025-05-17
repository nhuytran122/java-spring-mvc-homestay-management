package com.lullabyhomestay.homestay_management.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFoundException(NoHandlerFoundException ex, Model model) {
        return "shared/404";
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException ex, Model model) {
        String errorMessage = "Không tìm thấy " + ex.getEntityName();
        model.addAttribute("errorMessage", errorMessage);
        return "shared/not-found";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "shared/error";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "shared/auth/deny";
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex, Model model) {
        model.addAttribute("errorMessage", "Thông tin bạn nhập không hợp lệ. Vui lòng kiểm tra và thử lại.");
        return "shared/error";
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String handleJsonParseError(HttpMessageNotReadableException ex, Model model) {
        model.addAttribute("errorMessage",
                "Hệ thống không thể xử lý yêu cầu của bạn. Vui lòng kiểm tra lại thông tin và thử lại.");
        return "shared/error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn. Vui lòng thử lại sau.");
        return "shared/error";
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public String handleAuthError(AuthenticationCredentialsNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", "Bạn cần đăng nhập để tiếp tục.");
        return "shared/auth/deny";
    }

    @ExceptionHandler(DataAccessException.class)
    public String handleDatabaseError(DataAccessException ex, Model model) {
        model.addAttribute("errorMessage", "Có lỗi xảy ra với cơ sở dữ liệu. Vui lòng thử lại sau." + ex);
        return "shared/error";
    }

}