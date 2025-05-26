package com.lullabyhomestay.homestay_management.controller.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lullabyhomestay.homestay_management.domain.Rule;
import com.lullabyhomestay.homestay_management.service.RuleService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@PreAuthorize("hasAnyRole('MANAGER')")
@RequestMapping("/admin/homestay-infor/rule")
public class RuleController {
    private final RuleService ruleService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    public String getRulePage(Model model) {
        model.addAttribute("inactiveRules", ruleService.getRulesByIsHidden(true));
        model.addAttribute("activeRules", ruleService.getRulesByIsHidden(false));
        return "admin/rule/show";
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> postCreateRules(@RequestBody List<Rule> listRules) {
        for (Rule request : listRules) {
            Rule optionalRule = ruleService.getRuleByRuleID(request.getRuleID());
            optionalRule.setIsHidden(false);
            optionalRule.setDescription(request.getDescription());
            ruleService.handleSaveRule(optionalRule);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public String postUpdateRule(@RequestBody Rule rule) {
        Rule currentRule = this.ruleService.getRuleByRuleID(rule.getRuleID());
        currentRule.setDescription(rule.getDescription());
        this.ruleService.handleSaveRule(currentRule);
        return "redirect:/admin/homestay-infor/rule";
    }

    @PostMapping("/delete")
    public String postHiddenRule(Model model,
            @ModelAttribute("rule") Rule rule) {
        Rule currentRule = ruleService.getRuleByRuleID(rule.getRuleID());
        currentRule.setIsHidden(true);
        currentRule.setDescription("");
        this.ruleService.handleSaveRule(currentRule);
        return "redirect:/admin/homestay-infor/rule";
    }
}
