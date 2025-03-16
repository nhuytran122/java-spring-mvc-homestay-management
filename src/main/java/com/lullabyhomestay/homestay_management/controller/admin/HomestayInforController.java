package com.lullabyhomestay.homestay_management.controller.admin;

import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.lullabyhomestay.homestay_management.domain.HomestayDetail;
import com.lullabyhomestay.homestay_management.service.HomestayInforService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class HomestayInforController {
    private final HomestayInforService homestayService;

    @GetMapping("/admin/homestay-infor")
    public String getInforHomestayPage(Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String keyword) {
        int validPage = Math.max(1, page);
        Page<HomestayDetail> infors = homestayService.searchInforHomestay(keyword,
                validPage);

        List<HomestayDetail> listInfors = infors.getContent();
        model.addAttribute("infors", listInfors);

        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", validPage);
        model.addAttribute("totalPages", infors.getTotalPages());
        return "admin/homestay-infor/show";
    }

    @GetMapping("/admin/homestay-infor/{id}")
    @ResponseBody
    public ResponseEntity<?> getDetailInfor(@PathVariable("id") long id) {
        HomestayDetail infor = homestayService.getInforHomestayByInforID(id);
        return ResponseEntity.ok(infor);
    }

    @GetMapping("/admin/homestay-infor/create")
    public String getCreateInforPage(Model model) {
        model.addAttribute("newInfor", new HomestayDetail());
        return "admin/homestay-infor/create";
    }

    @PostMapping("/admin/homestay-infor/create")
    public String postCreateInfor(Model model,
            @ModelAttribute("newInfor") @Valid HomestayDetail faq,
            BindingResult newInforBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);

        if (newInforBindingResult.hasErrors()) {
            return "admin/homestay-infor/create";
        }

        this.homestayService.handleSaveInforHomestay(faq);
        return "redirect:/admin/homestay-infor";
    }

    @GetMapping("/admin/homestay-infor/update/{id}")
    public String getUpdateInforPage(Model model, @PathVariable long id) {
        HomestayDetail infor = homestayService.getInforHomestayByInforID(id);

        model.addAttribute("infor", infor);
        return "admin/homestay-infor/update";
    }

    @PostMapping("/admin/homestay-infor/update")
    public String postUpdateInfor(Model model,
            @ModelAttribute("infor") @Valid HomestayDetail infor,
            BindingResult newFaqBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);

        HomestayDetail currentInfor = this.homestayService.getInforHomestayByInforID(infor.getInforID());

        if (newFaqBindingResult.hasErrors()) {
            return "admin/homestay-infor/update";
        }
        currentInfor.setTitle(infor.getTitle());
        currentInfor.setDescription(infor.getDescription());
        this.homestayService.handleSaveInforHomestay(currentInfor);
        return "redirect:/admin/homestay-infor";
    }

    @PostMapping("/admin/homestay-infor/delete")
    public String postDeleteInfor(@RequestParam("inforID") long inforID) {
        this.homestayService.deleteByInforID(inforID);
        return "redirect:/admin/homestay-infor";
    }
}
