package com.lullabyhomestay.homestay_management.controller.admin;

import java.util.List;
import org.springframework.data.domain.Page;
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
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String keyword) {
        int validPage = Math.max(1, page);
        Page<FAQ> faqs = faqService.searchFAQs(keyword,
                validPage);

        List<FAQ> listFAQs = faqs.getContent();
        model.addAttribute("faqs", listFAQs);

        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", validPage);
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
        FAQ faq = faqService.getFAQByFAQID(id);

        model.addAttribute("faq", faq);
        return "admin/faq/update";
    }

    @PostMapping("/admin/homestay-infor/faq/update")
    public String postUpdateFaq(Model model,
            @ModelAttribute("faq") @Valid FAQ faq,
            BindingResult newFaqBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);

        FAQ currentFAQ = this.faqService.getFAQByFAQID(faq.getFaqID());

        if (newFaqBindingResult.hasErrors()) {
            return "admin/faq/update";
        }

        currentFAQ.setQuestion(faq.getQuestion());
        currentFAQ.setAnswer(faq.getAnswer());

        this.faqService.handleSaveFAQ(currentFAQ);
        return "redirect:/admin/homestay-infor/faq";
    }

    @PostMapping("/admin/homestay-infor/faq/delete")
    public String postDeleteFaq(@RequestParam("faqID") long faqID) {
        this.faqService.deleteByFAQID(faqID);
        return "redirect:/admin/homestay-infor/faq";
    }
}
