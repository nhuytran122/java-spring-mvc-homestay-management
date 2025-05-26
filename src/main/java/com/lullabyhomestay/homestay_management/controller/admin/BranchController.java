package com.lullabyhomestay.homestay_management.controller.admin;

import java.util.List;

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
import org.springframework.web.multipart.MultipartFile;

import com.lullabyhomestay.homestay_management.domain.Branch;
import com.lullabyhomestay.homestay_management.service.BranchService;
import com.lullabyhomestay.homestay_management.service.UploadService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@PreAuthorize("hasRole('MANAGER')")
@RequestMapping("/admin/branch")
public class BranchController {

    private final BranchService branchService;
    private final UploadService uploadService;

    @PreAuthorize("permitAll()")
    @GetMapping("")
    public String getBranchPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String keyword) {
        int validPage = Math.max(1, page);

        Page<Branch> branches = branchService.searchBranches(keyword, validPage);

        List<Branch> listBranches = branches.getContent();
        model.addAttribute("branches", listBranches);

        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", validPage);
        model.addAttribute("totalPages", branches.getTotalPages());
        return "admin/branch/show";
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public String getDetailBranchPage(Model model, @PathVariable long id) {
        Branch branch = branchService.getBranchByID(id);
        model.addAttribute("branch", branch);
        return "admin/branch/detail";
    }

    @GetMapping("/create")
    public String getCreateBranchPage(Model model) {
        model.addAttribute("newBranch", new Branch());
        return "admin/branch/create";
    }

    @PostMapping("/create")
    public String postCreateBranch(Model model,
            @ModelAttribute("newBranch") @Valid Branch branch,
            BindingResult newBranchBindingResult,
            @RequestParam("fileImg") MultipartFile file) {

        if (newBranchBindingResult.hasErrors()) {
            return "admin/branch/create";
        }

        String img;
        if (!file.isEmpty()) {
            img = this.uploadService.handleSaveUploadFile(file, "branch");
            branch.setImage(img);
        }

        this.branchService.handleSaveBranch(branch);
        return "redirect:/admin/branch";
    }

    @GetMapping("/update/{id}")
    public String getUpdateBranchPage(Model model, @PathVariable long id) {
        Branch branch = branchService.getBranchByID(id);

        model.addAttribute("branch", branch);
        return "admin/branch/update";
    }

    @PostMapping("/update")
    public String postUpdateBranch(Model model,
            @ModelAttribute("branch") @Valid Branch branch,
            BindingResult newBranchBindingResult,
            @RequestParam("fileImg") MultipartFile file,
            HttpServletRequest request) {
        if (newBranchBindingResult.hasErrors()) {
            return "admin/branch/update";
        }
        Branch currentBranch = this.branchService.getBranchByID(branch.getBranchID());

        if (!file.isEmpty()) {
            String img = this.uploadService.handleSaveUploadFile(file, "branch");
            currentBranch.setImage(img);
        }
        currentBranch.setBranchName(branch.getBranchName());
        currentBranch.setAddress(branch.getAddress());
        currentBranch.setPhone(branch.getPhone());
        currentBranch.setGatePassword(branch.getGatePassword());

        this.branchService.handleSaveBranch(currentBranch);

        return "redirect:/admin/branch";
    }

    @GetMapping("/can-delete/{id}")
    public ResponseEntity<Boolean> canDeleteBranch(@PathVariable long id) {
        boolean canDelete = branchService.canDeleteBranch(id);
        return ResponseEntity.ok(canDelete);
    }

    @PostMapping("/delete")
    public String postDeleteBranch(@RequestParam("branchID") long branchID) {
        this.branchService.deleteByBranchID(branchID);
        return "redirect:/admin/branch";
    }

}
