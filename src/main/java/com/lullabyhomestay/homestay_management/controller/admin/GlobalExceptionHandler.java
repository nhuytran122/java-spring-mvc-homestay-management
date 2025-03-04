package com.lullabyhomestay.homestay_management.controller.admin;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lullabyhomestay.homestay_management.domain.InventoryTransaction;
import com.lullabyhomestay.homestay_management.exception.InventoryException;
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
        model.addAttribute("newExport", new InventoryTransaction());
        return "admin/warehouse/export";
    }
}