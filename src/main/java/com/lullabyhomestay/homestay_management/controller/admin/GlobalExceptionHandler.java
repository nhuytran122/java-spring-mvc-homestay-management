package com.lullabyhomestay.homestay_management.controller.admin;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.lullabyhomestay.homestay_management.exception.InventoryException;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.BranchRepository;
import com.lullabyhomestay.homestay_management.repository.InventoryItemRepository;

import lombok.AllArgsConstructor;

@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {
    private final BranchRepository branchRepository;
    private final InventoryItemRepository itemRepository;

    @ExceptionHandler(InventoryException.class)
    public String handleInventoryTransactionException(InventoryException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("listItems", itemRepository.findAll());
        model.addAttribute("listBranches", branchRepository.findAll());
        model.addAttribute("newExport", ex.getData());
        return "admin/warehouse/export";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFoundException(NoHandlerFoundException ex, Model model) {
        return "/admin/404";
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException ex, Model model) {
        String errorMessage = "Không tìm thấy " + ex.getEntityName();
        model.addAttribute("errorMessage", errorMessage);
        return "/admin/not-found";
    }
}