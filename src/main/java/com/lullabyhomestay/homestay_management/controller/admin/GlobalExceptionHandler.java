package com.lullabyhomestay.homestay_management.controller.admin;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.lullabyhomestay.homestay_management.exception.NotFoundException;

import lombok.AllArgsConstructor;

@ControllerAdvice
@AllArgsConstructor
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
}