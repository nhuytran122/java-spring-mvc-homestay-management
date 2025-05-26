package com.lullabyhomestay.homestay_management.controller.admin;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.domain.Employee;
import com.lullabyhomestay.homestay_management.domain.InventoryStock;
import com.lullabyhomestay.homestay_management.domain.InventoryTransaction;
import com.lullabyhomestay.homestay_management.domain.User;
import com.lullabyhomestay.homestay_management.domain.dto.EmployeeDTO;
import com.lullabyhomestay.homestay_management.domain.dto.SearchTransactionCriterialDTO;
import com.lullabyhomestay.homestay_management.service.BranchService;
import com.lullabyhomestay.homestay_management.service.EmployeeService;
import com.lullabyhomestay.homestay_management.service.InventoryItemService;
import com.lullabyhomestay.homestay_management.service.InventoryStockService;
import com.lullabyhomestay.homestay_management.service.InventoryTransactionService;
import com.lullabyhomestay.homestay_management.service.UserService;
import com.lullabyhomestay.homestay_management.utils.TransactionType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE')")
@RequestMapping("/admin/warehouse")
public class WarehouseController {
    private final InventoryStockService stockService;
    private final BranchService branchService;
    private final InventoryTransactionService transactionService;
    private final InventoryItemService itemService;
    private final EmployeeService employeeService;
    private final ModelMapper mapper;
    private final UserService userService;

    @GetMapping("")
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

    @GetMapping("/import")
    public String getImportItemPage(Model model,
            @RequestParam(required = false) Long branchID,
            @RequestParam(required = false) Long itemID) {
        InventoryTransaction transaction = createTransactionFromParams(branchID, itemID);
        prepareTransactionModel(model, transaction, "newImport");
        return "admin/warehouse/import";
    }

    @PostMapping("/import")
    public String postImportItem(Model model,
            @ModelAttribute("newImport") @Valid InventoryTransaction transaction,
            BindingResult bindingResult,
            HttpServletRequest request) {

        return handleTransaction(model, transaction, bindingResult, TransactionType.IMPORT, "newImport",
                "admin/warehouse/import", request);
    }

    @GetMapping("/export")
    public String getExportItemPage(Model model,
            @RequestParam(required = false) Long branchID,
            @RequestParam(required = false) Long itemID) {
        InventoryTransaction transaction = createTransactionFromParams(branchID, itemID);
        prepareTransactionModel(model, transaction, "newExport");
        return "admin/warehouse/export";
    }

    @PostMapping("/export")
    public String postExportItem(Model model,
            @ModelAttribute("newExport") @Valid InventoryTransaction transaction,
            BindingResult bindingResult,
            HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            prepareTransactionModel(model, transaction, "newExport");
            return "admin/warehouse/export";
        }

        Optional<InventoryStock> currentStock = stockService.findStockByItemIDAndBranchID(
                transaction.getInventoryItem().getItemID(), transaction.getBranch().getBranchID());

        if (!currentStock.isPresent()) {
            model.addAttribute("error", "Không tìm thấy đồ dùng trong chi nhánh này");
            prepareTransactionModel(model, transaction, "newExport");
            return "admin/warehouse/export";
        }

        if (currentStock.get().getQuantity() < transaction.getQuantity()) {
            model.addAttribute("error",
                    "Số lượng xuất vượt quá tồn kho. Chỉ còn " + currentStock.get().getQuantity()
                            + " " + currentStock.get().getInventoryItem().getItemName() + " trong kho");
            prepareTransactionModel(model, transaction, "newExport");
            return "admin/warehouse/export";
        }

        EmployeeDTO employeeDTO = getLoggedInEmployeeDTO(request);
        mapAndSetEmployeeToTransaction(transaction, employeeDTO);
        transaction.setTransactionType(TransactionType.EXPORT);
        transactionService.handleSaveTransaction(transaction);

        return "redirect:/admin/warehouse";
    }

    @GetMapping("/transaction")
    public String getHistoryTransactionPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @ModelAttribute SearchTransactionCriterialDTO criteria) {
        int validPage = Math.max(1, page);
        String sort = (criteria.getSort() != null && !criteria.getSort().isEmpty()) ? criteria.getSort() : "desc";
        criteria.setSort(sort);
        Page<InventoryTransaction> transactions = transactionService.searchTransactions(criteria, validPage);
        List<InventoryTransaction> listTransactions = transactions.getContent();

        model.addAttribute("criteria", criteria);
        model.addAttribute("extraParams", criteria.convertToExtraParams());
        model.addAttribute("listTransactions", listTransactions);
        model.addAttribute("currentPage", validPage);
        model.addAttribute("totalPages", transactions.getTotalPages());
        model.addAttribute("transactionTypes", TransactionType.values());
        model.addAttribute("listBranches", this.branchService.getAllBranches());
        return "admin/warehouse/transaction/show";
    }

    @GetMapping("/transaction/can-update/{id}")
    public ResponseEntity<Boolean> canUpdateTransaction(@PathVariable Long id) {
        boolean canUpdate = transactionService.canUpdateTransaction(id);
        return ResponseEntity.ok(canUpdate);
    }

    @GetMapping("/transaction/update/{id}")
    public String getUpdateTransactionPage(Model model, @PathVariable Long id) {
        InventoryTransaction transactionOpt = transactionService.getTransactionByID(id);
        model.addAttribute("transaction", transactionOpt);
        return "admin/warehouse/transaction/update";
    }

    @PostMapping("/transaction/update")
    public String postUpdateTransaction(Model model,
            @ModelAttribute("transaction") @Valid InventoryTransaction transaction,
            BindingResult result,
            HttpServletRequest request) {

        Long transactionId = transaction.getTransactionID();
        InventoryTransaction currentTransaction = this.transactionService.getTransactionByID(transactionId);
        if (result.hasErrors()) {
            return "admin/warehouse/transaction/update";
        }
        Optional<InventoryStock> currentStock = stockService.findStockByItemIDAndBranchID(
                currentTransaction.getInventoryItem().getItemID(), currentTransaction.getBranch().getBranchID());
        if (transaction.getTransactionType() == TransactionType.EXPORT) {
            if (!currentStock.isPresent()) {
                model.addAttribute("error", "Không tìm thấy đồ dùng trong chi nhánh này");
            }
            if (currentStock.get().getQuantity() < transaction.getQuantity()) {
                model.addAttribute("error",
                        "Số lượng xuất vượt quá tồn kho. Chỉ còn " + currentStock.get().getQuantity()
                                + " " + currentStock.get().getInventoryItem().getItemName() + " trong kho");
                model.addAttribute("transaction", transaction);
                return "admin/warehouse/transaction/update";
            }
        }
        int oldQuantity = currentTransaction.getQuantity();
        currentTransaction.setQuantity(transaction.getQuantity());
        currentTransaction.setUpdatedAt(LocalDateTime.now());
        this.transactionService.updateTransaction(currentTransaction, oldQuantity);
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
            InventoryStock stock = stockService.findStockByItemIDAndBranchID(itemID, branchID).get();
            transaction.setInventoryItem(stock.getInventoryItem());
            transaction.setBranch(stock.getBranch());
        }
        return transaction;
    }

    private String handleTransaction(Model model, @Valid InventoryTransaction transaction, BindingResult bindingResult,
            TransactionType transactionType, String modelAttributeName, String viewName, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            prepareTransactionModel(model, transaction, modelAttributeName);
            return viewName;
        }
        EmployeeDTO employeeDTO = getLoggedInEmployeeDTO(request);
        mapAndSetEmployeeToTransaction(transaction, employeeDTO);
        transaction.setTransactionType(transactionType);
        transactionService.handleSaveTransaction(transaction);
        return "redirect:/admin/warehouse";
    }

    private EmployeeDTO getLoggedInEmployeeDTO(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute("id");
        User user = userService.getUserByUserID(userId);
        EmployeeDTO employeeDTO = employeeService.getEmployeeDTOByID(user.getEmployee().getEmployeeID());
        return employeeDTO;
    }

    private void mapAndSetEmployeeToTransaction(InventoryTransaction transaction, EmployeeDTO employeeDTO) {
        if (employeeDTO != null) {
            transaction.setEmployee(mapper.map(employeeDTO, Employee.class));
        }
    }
}
