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
import com.lullabyhomestay.homestay_management.domain.FAQ;
import com.lullabyhomestay.homestay_management.service.FAQService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class FAQController {

    private final FAQService faqService;

    @GetMapping("/admin/homestay-infor/faq")
    public String getFaqPage(Model model,
            @RequestParam("page") Optional<String> pageOptional,
            @RequestParam("keyword") Optional<String> keyword) {
        int page = 1;
        try {
            if (pageOptional.isPresent()) {
                page = Integer.parseInt(pageOptional.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Pageable pageable = PageRequest.of(page - 1, 2);
        Page<FAQ> faqs = (keyword.isPresent()) 
        ? faqService.searchFAQs(keyword.get(), pageable) 
        : faqService.getAllFAQs(pageable);

        List<FAQ> listFAQs = faqs.getContent();
        model.addAttribute("faqs", listFAQs);

        model.addAttribute("keyword", keyword.orElse(""));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", faqs.getTotalPages());
        return "admin/faq/show";
    }

    @GetMapping("/admin/homestay-infor/faq/create")
    public String getCreateFaqPage(Model model) {
        model.addAttribute("newFAQ", new FAQ());
        return "admin/faq/create";
    }

    @PostMapping("/admin/homestay-infor/faq/create")
    public String postCreateFaq(Model model,
            @ModelAttribute("newFAQ") @Valid FAQ faq,
            BindingResult newFaqBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);

        if (newFaqBindingResult.hasErrors()) {
            return "admin/faq/create";
        }

        this.faqService.handleSaveFAQ(faq);
        return "redirect:/admin/homestay-infor/faq";
    }

    @GetMapping("/admin/homestay-infor/faq/update/{id}")
    public String getUpdateFaqPage(Model model, @PathVariable long id) {
        Optional<FAQ> faq = faqService.getFAQByFAQID(id);
        if (!faq.isPresent()) {
            return "admin/homestay-infor/faq";
        }
        
        model.addAttribute("faq", faq.get());
        return "admin/faq/update";
    }

    @PostMapping("/admin/homestay-infor/faq/update")
    public String postUpdateFaq(Model model,
            @ModelAttribute("faq") @Valid FAQ faq,
            BindingResult newFaqBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);
        
        FAQ currentFAQ = this.faqService.getFAQByFAQID(faq.getFaqID()).get();

        if (newFaqBindingResult.hasErrors()) {
            return "admin/faq/update";
        }

        if (currentFAQ != null) {
            currentFAQ.setQuestion(faq.getQuestion());
            currentFAQ.setAnswer(faq.getAnswer());

            this.faqService.handleSaveFAQ(currentFAQ);
        }
        return "redirect:/admin/homestay-infor/faq";
    }

    @PostMapping("/admin/homestay-infor/faq/delete")
    public String postDeleteFaq(@RequestParam("faqID") long faqID) {
        this.faqService.deleteByFAQID(faqID);
        return "redirect:/admin/homestay-infor/faq";
    }
}
