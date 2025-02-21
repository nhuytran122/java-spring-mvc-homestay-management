package com.lullabyhomestay.homestay_management.controller.admin;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lullabyhomestay.homestay_management.domain.Rule;
import com.lullabyhomestay.homestay_management.service.RuleService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class RuleController {
    private final RuleService ruleService;

    @GetMapping("/admin/homestay-infor/rule")
    public String getRulePage(Model model) {
        model.addAttribute("rules", ruleService.getAllRules());
        return "admin/rule/show";
    }

    @GetMapping("/admin/homestay-infor/rule/create")
    public String getCreateRulePage(Model model) {
        model.addAttribute("newRule", new Rule());
        return "admin/rule/create";
    }

    @PostMapping("/admin/homestay-infor/rule/create")
    public String postCreateRule(Model model,
            @ModelAttribute("newRule") @Valid Rule fule,
            BindingResult newRuleBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);

        if (newRuleBindingResult.hasErrors()) {
            return "admin/rule/create";
        }

        this.ruleService.handleSaveRule(fule);
        return "redirect:/admin/homestay-infor/rule";
    }

    @GetMapping("/admin/homestay-infor/rule/update/{id}")
    public String getUpdateRulePage(Model model, @PathVariable long id) {
        Optional<Rule> rule = ruleService.getRuleByRuleID(id);
        if (!rule.isPresent()) {
            return "admin/homestay-infor/rule";
        }

        model.addAttribute("rule", rule.get());
        return "admin/rule/update";
    }

    @PostMapping("/admin/homestay-infor/rule/update")
    public String postUpdateRule(Model model,
            @ModelAttribute("rule") @Valid Rule rule,
            BindingResult newRuleBindingResult,
            HttpServletRequest request) {

        // HttpSession session = request.getSession(false);

        Rule currentRule = this.ruleService.getRuleByRuleID(rule.getRuleID()).get();

        if (newRuleBindingResult.hasErrors()) {
            return "admin/rule/update";
        }

        if (currentRule != null) {
            currentRule.setRuleTitle(rule.getRuleTitle());
            currentRule.setDescription(rule.getDescription());
            // currentRule.setIsFixed(currentRule.getIsFixed());
            currentRule.setIsHidden(rule.getIsHidden());
            this.ruleService.handleSaveRule(currentRule);
        }
        return "redirect:/admin/homestay-infor/rule";
    }

    @PostMapping("/admin/homestay-infor/rule/delete")
    public String postDeleteRule(@RequestParam("ruleID") long ruleID) {
        this.ruleService.deleteByRuleID(ruleID);
        return "redirect:/admin/homestay-infor/rule";
    }
}
