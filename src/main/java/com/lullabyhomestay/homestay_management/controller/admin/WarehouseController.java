package com.lullabyhomestay.homestay_management.controller.admin;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.hibernate.dialect.function.TransactSQLStrFunction;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.lullabyhomestay.homestay_management.domain.Branch;
import com.lullabyhomestay.homestay_management.domain.InventoryStock;
import com.lullabyhomestay.homestay_management.domain.InventoryTransaction;
import com.lullabyhomestay.homestay_management.domain.InventoryTransaction_;
import com.lullabyhomestay.homestay_management.domain.Room;
import com.lullabyhomestay.homestay_management.domain.dto.SearchRoomCriteriaDTO;
import com.lullabyhomestay.homestay_management.domain.dto.SearchTransactionCriterialDTO;
import com.lullabyhomestay.homestay_management.service.BranchService;
import com.lullabyhomestay.homestay_management.service.InventoryItemService;
import com.lullabyhomestay.homestay_management.service.InventoryStockService;
import com.lullabyhomestay.homestay_management.service.InventoryTransactionService;
import com.lullabyhomestay.homestay_management.utils.TransactionType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class WarehouseController {
    private final InventoryStockService stockService;
    private final BranchService branchService;
    private InventoryTransactionService transactionService;
    private InventoryItemService itemService;

    @GetMapping("/admin/warehouse")
    public String getInventoryStockPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) Long branchID) {
        int validPage = Math.max(1, page);

        Page<InventoryStock> stocks = stockService.searchStocks(keyword, branchID, validPage);
        List<InventoryStock> listStocks = stocks.getContent();

        StringBuilder extraParams = new StringBuilder();
        if (branchID != null) {
            extraParams.append("&branchID=").append(branchID);
        }
        if (keyword != null && !keyword.isEmpty()) {
            extraParams.append("&keyword=").append(URLEncoder.encode(keyword, StandardCharsets.UTF_8));
        }
        model.addAttribute("extraParams", extraParams);
        model.addAttribute("listStocks", listStocks);
        model.addAttribute("currentPage", validPage);
        model.addAttribute("totalPages", stocks.getTotalPages());

        model.addAttribute("listBranches", this.branchService.getAllBranches());
        model.addAttribute("keyword", keyword);
        model.addAttribute("branchID", branchID);
        return "admin/warehouse/show";
    }

    @GetMapping("/admin/warehouse/import")
    public String getImportItemPage(Model model,
            @RequestParam(required = false) Long branchID,
            @RequestParam(required = false) Long itemID) {
        InventoryTransaction transaction = createTransactionFromParams(branchID, itemID);
        prepareTransactionModel(model, transaction, "newImport");
        return "admin/warehouse/import";
    }

    @PostMapping("/admin/warehouse/import")
    public String postImportItem(Model model,
            @ModelAttribute("newImport") @Valid InventoryTransaction transaction,
            BindingResult bindingResult,
            HttpServletRequest request) {
        return handleTransaction(model, transaction, bindingResult, TransactionType.IMPORT, "newImport",
                "admin/warehouse/import");
    }

    @GetMapping("/admin/warehouse/export")
    public String getExportItemPage(Model model,
            @RequestParam(required = false) Long branchID,
            @RequestParam(required = false) Long itemID) {
        InventoryTransaction transaction = createTransactionFromParams(branchID, itemID);
        prepareTransactionModel(model, transaction, "newExport");
        return "admin/warehouse/export";
    }

    @PostMapping("/admin/warehouse/export")
    public String postExportItem(Model model,
            @ModelAttribute("newExport") @Valid InventoryTransaction transaction,
            BindingResult bindingResult,
            HttpServletRequest request) {
        return handleTransaction(model, transaction, bindingResult, TransactionType.EXPORT, "newExport",
                "admin/warehouse/export");
    }

    @GetMapping("/admin/warehouse/transaction")
    public String getHistoryTransactionPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @ModelAttribute SearchTransactionCriterialDTO criteria) {
        int validPage = Math.max(1, page);
        Page<InventoryTransaction> transactions = transactionService.searchTransactions(criteria.getKeyword(),
                criteria.getBranchID(), validPage,
                criteria.getSort());
        List<InventoryTransaction> listTransactions = transactions.getContent();

        model.addAttribute("criteria", criteria);
        model.addAttribute("extraParams", criteria.convertToExtraParams());
        model.addAttribute("listTransactions", listTransactions);
        model.addAttribute("currentPage", validPage);
        model.addAttribute("totalPages", transactions.getTotalPages());

        model.addAttribute("listBranches", this.branchService.getAllBranches());
        return "admin/warehouse/transaction/show";
    }

    @GetMapping("/admin/warehouse/transaction/can-update/{id}")
    public ResponseEntity<Boolean> canUpdateTransaction(@PathVariable Long id) {
        boolean canUpdate = transactionService.canUpdateTransaction(id);
        return ResponseEntity.ok(canUpdate);
    }

    @GetMapping("/admin/warehouse/transaction/update/{id}")
    public String getUpdateTransactionPage(Model model, @PathVariable Long id) {
        Optional<InventoryTransaction> transactionOpt = transactionService.getTransactionByID(id);
        if (!transactionOpt.isPresent()) {
            return "admin/warehouse/transaction";
        }

        model.addAttribute("transaction", transactionOpt.get());
        return "admin/warehouse/transaction/update";
    }

    @PostMapping("/admin/warehouse/transaction/update")
    public String postUpdateTransaction(Model model,
            @ModelAttribute("transaction") @Valid InventoryTransaction transaction,
            BindingResult result,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
        Long transactionId = transaction.getTransactionID();
        InventoryTransaction currentTransaction = this.transactionService.getTransactionByID(transactionId).get();
        if (result.hasErrors()) {
            return "admin/warehouse/transaction/update";
        }
        if (currentTransaction != null) {
            int oldQuantity = currentTransaction.getQuantity();
            currentTransaction.setQuantity(transaction.getQuantity());
            currentTransaction.setDate(LocalDateTime.now());
            this.transactionService.updateTransaction(currentTransaction, oldQuantity);
        }
        return "redirect:/admin/warehouse/transaction";
    }

    private void prepareTransactionModel(Model model, InventoryTransaction transaction, String modelAttributeName) {
        model.addAttribute(modelAttributeName, transaction);
        model.addAttribute("listItems", itemService.getAllItems());
        model.addAttribute("listBranches", branchService.getAllBranches());
    }

    private InventoryTransaction createTransactionFromParams(Long branchID, Long itemID) {
        InventoryTransaction transaction = new InventoryTransaction();
        if (branchID != null && itemID != null) {
            Optional<InventoryStock> stockOptional = stockService.findStockByItemIDAndBranchID(itemID, branchID);
            if (stockOptional.isPresent()) {
                InventoryStock stock = stockOptional.get();
                transaction.setInventoryItem(stock.getInventoryItem());
                transaction.setBranch(stock.getBranch());
            }
        }
        return transaction;
    }

    private String handleTransaction(Model model, @Valid InventoryTransaction transaction, BindingResult bindingResult,
            TransactionType transactionType, String modelAttributeName, String viewName) {
        if (transaction.getBranch() == null || transaction.getBranch().getBranchID() == null) {
            bindingResult.rejectValue("branch", "error.branch", "Vui lòng chọn chi nhánh");
        }
        if (transaction.getInventoryItem() == null || transaction.getInventoryItem().getItemID() == null) {
            bindingResult.rejectValue("inventoryItem", "error.inventoryItem", "Vui lòng chọn đồ dùng");
        }

        if (bindingResult.hasErrors()) {
            prepareTransactionModel(model, transaction, modelAttributeName);
            return viewName;
        }
        transaction.setTransactionType(transactionType);
        transactionService.handleSaveTransaction(transaction);
        return "redirect:/admin/warehouse";
    }
}
