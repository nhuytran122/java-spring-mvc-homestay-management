package com.lullabyhomestay.homestay_management.controller.admin;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.lullabyhomestay.homestay_management.domain.InventoryStock;
import com.lullabyhomestay.homestay_management.domain.InventoryTransaction;
import com.lullabyhomestay.homestay_management.domain.Branch;
import com.lullabyhomestay.homestay_management.service.BranchService;
import com.lullabyhomestay.homestay_management.service.InventoryCategoryService;
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
    public String getImportItemPage(Model model) {
        model.addAttribute("newImport", new InventoryTransaction());
        model.addAttribute("listItems", itemService.getAllItems());
        model.addAttribute("listBranches", branchService.getAllBranches());
        return "admin/warehouse/import";
    }

    @PostMapping("/admin/warehouse/import")
    public String postImportItem(Model model,
            @ModelAttribute("newImport") @Valid InventoryTransaction transaction,
            BindingResult newTransactionBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);

        if (newTransactionBindingResult.hasErrors()) {
            model.addAttribute("listItems", itemService.getAllItems());
            model.addAttribute("listBranches", branchService.getAllBranches());
            return "admin/warehouse/import";
        }
        transaction.setTransactionType(TransactionType.IMPORT);
        this.transactionService.handleSaveTransaction(transaction);
        return "redirect:/admin/warehouse";
    }

    @GetMapping("/admin/warehouse/export")
    public String getExportItemPage(Model model) {
        model.addAttribute("newExport", new InventoryTransaction());
        model.addAttribute("listItems", itemService.getAllItems());
        model.addAttribute("listBranches", branchService.getAllBranches());
        return "admin/warehouse/export";
    }

    @PostMapping("/admin/warehouse/export")
    public String postExportItem(Model model,
            @ModelAttribute("newExport") @Valid InventoryTransaction transaction,
            BindingResult newTransactionBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);

        if (newTransactionBindingResult.hasErrors()) {
            model.addAttribute("listItems", itemService.getAllItems());
            model.addAttribute("listBranches", branchService.getAllBranches());
            return "admin/warehouse/export";
        }
        transaction.setTransactionType(TransactionType.EXPORT);
        this.transactionService.handleSaveTransaction(transaction);
        return "redirect:/admin/warehouse";
    }
}
