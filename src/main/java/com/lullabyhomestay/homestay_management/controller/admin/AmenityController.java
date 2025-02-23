package com.lullabyhomestay.homestay_management.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.domain.Amenity;
import com.lullabyhomestay.homestay_management.service.AmenityCategoryService;
import com.lullabyhomestay.homestay_management.service.AmenityService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class AmenityController {
    private final AmenityCategoryService categoryService;
    private final AmenityService amenityService;

    @GetMapping("/admin/amenity")
    public String getAmenityPage(Model model,
            @RequestParam("page") Optional<String> pageOptional,
            @RequestParam("keyword") Optional<String> keyword,
            @RequestParam("categoryID") Optional<Long> categoryID) {
        int page = 1;
        try {
            if (pageOptional.isPresent()) {
                page = Integer.parseInt(pageOptional.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Pageable pageable = PageRequest.of(page - 1, 2);
        Page<Amenity> amenities = (keyword.isPresent() || categoryID.isPresent())
                ? amenityService.searchAmenities(keyword.get(), categoryID.get(), pageable)
                : amenityService.getAllAmenities(pageable);

        List<Amenity> listAmenities = amenities.getContent();
        model.addAttribute("listCategories", this.categoryService.getAllAmenityCategories());
        model.addAttribute("amenities", listAmenities);
        model.addAttribute("keyword", keyword.orElse(""));
        model.addAttribute("categoryID", categoryID.orElse(0L));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", amenities.getTotalPages());
        return "admin/amenity/show";
    }

    // @GetMapping("/admin/amenity/{id}")
    // @ResponseBody
    // public ResponseEntity<?> getDetailInfor(@PathVariable("id") long id) {
    // Optional<Amenity> infor = amenityService.getInforHomestayByInforID(id);
    // if (!infor.isPresent()) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy thông
    // tin!");
    // }
    // return ResponseEntity.ok(infor.get());
    // }

    @GetMapping("/admin/amenity/create")
    public String getCreateAmenityPage(Model model) {
        model.addAttribute("newAmenity", new Amenity());
        model.addAttribute("listCategories", this.categoryService.getAllAmenityCategories());
        return "admin/amenity/create";
    }

    @PostMapping("/admin/amenity/create")
    public String postCreateInfor(Model model,
            @ModelAttribute("newAmenity") @Valid Amenity amenity,
            BindingResult newAmenityBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);

        if (newAmenityBindingResult.hasErrors()) {
            model.addAttribute("listCategories", this.categoryService.getAllAmenityCategories());
            return "admin/amenity/create";
        }
        this.amenityService.handleSaveAmenity(amenity);
        return "redirect:/admin/amenity";
    }

    @GetMapping("/admin/amenity/update/{id}")
    public String getUpdateInforPage(Model model, @PathVariable long id) {
        Optional<Amenity> amenity = amenityService.getAmenityByID(id);
        if (!amenity.isPresent()) {
            return "admin/amenity";
        }

        model.addAttribute("amenity", amenity.get());
        model.addAttribute("listCategories", this.categoryService.getAllAmenityCategories());
        return "admin/amenity/update";
    }

    @PostMapping("/admin/amenity/update")
    public String postUpdateInfor(Model model,
            @ModelAttribute("amenity") @Valid Amenity amenity,
            BindingResult newAmenityBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);

        Amenity currentAmenity = this.amenityService.getAmenityByID(amenity.getAmenityID()).get();

        if (amenity.getAmenityCategory().getCategoryID() == 0) {
            newAmenityBindingResult.rejectValue("amenityCategory",
                    "error.amenityCategory", "Vui lòng chọn phân loại hợp lệ.");
        }

        if (newAmenityBindingResult.hasErrors()) {
            model.addAttribute("listCategories", this.categoryService.getAllAmenityCategories());
            return "admin/amenity/update";
        }

        if (currentAmenity != null) {
            currentAmenity.setAmenityName(amenity.getAmenityName());
            currentAmenity.setAmenityCategory(amenity.getAmenityCategory());
            this.amenityService.handleSaveAmenity(currentAmenity);
        }
        return "redirect:/admin/amenity";
    }

    @PostMapping("/admin/amenity/delete")
    public String postDeleteAmenity(@RequestParam("amenityID") long amenityID) {
        this.amenityService.deleteByAmenityID(amenityID);
        return "redirect:/admin/amenity";
    }

}
