package com.lullabyhomestay.homestay_management.controller.admin;

import java.util.List;
import java.util.Optional;

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
import com.lullabyhomestay.homestay_management.service.BranchService;
import com.lullabyhomestay.homestay_management.service.UploadService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class BranchController {

    private final BranchService branchService;
    private final UploadService uploadService;

    @GetMapping("/admin/branch")
    public String getBranchPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String keyword) {
        int validPage = Math.max(1, page);

        Page<Branch> branches = branchService.searchBranches(keyword, validPage);

        List<Branch> listBranches = branches.getContent();
        model.addAttribute("branches", listBranches);

        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", branches.getTotalPages());
        return "admin/branch/show";
    }

    @GetMapping("/admin/branch/{id}")
    public String getDetailBranchPage(Model model, @PathVariable long id) {
        Optional<Branch> branch = branchService.getBranchByID(id);
        if (!branch.isPresent()) {
            return "admin/branch";
        }
        model.addAttribute("branch", branch.get());
        return "admin/branch/detail";
    }

    @GetMapping("/admin/branch/create")
    public String getCreateBranchPage(Model model) {
        model.addAttribute("newBranch", new Branch());
        return "admin/branch/create";
    }

    @PostMapping("/admin/branch/create")
    public String postCreateBranch(Model model,
            @ModelAttribute("newBranch") @Valid Branch branch,
            BindingResult newBranchBindingResult,
            @RequestParam("fileImg") MultipartFile file,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);

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

    @GetMapping("/admin/branch/update/{id}")
    public String getUpdateBranchPage(Model model, @PathVariable long id) {
        Optional<Branch> branch = branchService.getBranchByID(id);
        if (!branch.isPresent()) {
            return "admin/branch";
        }

        model.addAttribute("branch", branch.get());
        return "admin/branch/update";
    }

    @PostMapping("/admin/branch/update")
    public String postUpdateBranch(Model model,
            @ModelAttribute("branch") @Valid Branch branch,
            BindingResult newBranchBindingResult,
            @RequestParam("fileImg") MultipartFile file,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);

        Branch currentBranch = this.branchService.getBranchByID(branch.getBranchID()).get();

        if (newBranchBindingResult.hasErrors()) {
            return "admin/branch/update";
        }

        if (currentBranch != null) {
            if (!file.isEmpty()) {
                String img = this.uploadService.handleSaveUploadFile(file, "branch");
                currentBranch.setImage(img);
            }
            currentBranch.setBranchName(branch.getBranchName());
            currentBranch.setAddress(branch.getAddress());
            currentBranch.setPhone(branch.getPhone());
            currentBranch.setBranchPassword(branch.getBranchPassword());

            this.branchService.handleSaveBranch(currentBranch);
        }
        return "redirect:/admin/branch";
    }

    @GetMapping("/admin/branch/can-delete/{id}")
    public ResponseEntity<Boolean> canDeleteBranch(@PathVariable long id) {
        boolean canDelete = branchService.canDeleteBranch(id);
        return ResponseEntity.ok(canDelete);
    }

    @PostMapping("/admin/branch/delete")
    public String postDeleteBranch(@RequestParam("branchID") long branchID) {
        this.branchService.deleteByBranchID(branchID);
        return "redirect:/admin/branch";
    }

}
