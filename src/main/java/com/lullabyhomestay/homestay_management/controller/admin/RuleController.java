package com.lullabyhomestay.homestay_management.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lullabyhomestay.homestay_management.domain.Rule;
import com.lullabyhomestay.homestay_management.service.RuleService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class RuleController {
    private final RuleService ruleService;

    @GetMapping("/admin/homestay-infor/rule")
    public String getRulePage(Model model) {
        model.addAttribute("inactiveRules", ruleService.getRulesByIsHidden(true));
        model.addAttribute("activeRules", ruleService.getRulesByIsHidden(false));
        return "admin/rule/show";
    }

    @PostMapping("/admin/homestay-infor/rule/create")
    @ResponseBody
    public ResponseEntity<?> postCreateRules(@RequestBody List<Rule> listRules) {
        for (Rule request : listRules) {
            Optional<Rule> optionalRule = ruleService.getRuleByRuleID(request.getRuleID());
            if (optionalRule.isPresent()) {
                Rule currentRule = optionalRule.get();
                currentRule.setIsHidden(false);
                currentRule.setDescription(request.getDescription());
                ruleService.handleSaveRule(currentRule);
            }
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/homestay-infor/rule/update")
    public String postUpdateRule(@RequestBody Rule rule) {
        Rule currentRule = this.ruleService.getRuleByRuleID(rule.getRuleID()).get();
        if (currentRule != null) {
            currentRule.setDescription(rule.getDescription());
            this.ruleService.handleSaveRule(currentRule);
        }
        return "redirect:/admin/homestay-infor/rule";
    }

    @PostMapping("/admin/homestay-infor/rule/delete")
    public String postHiddenRule(Model model,
            @ModelAttribute("rule") Rule rule) {
        Rule currentRule = ruleService.getRuleByRuleID(rule.getRuleID()).get();
        currentRule.setIsHidden(true);
        currentRule.setDescription("");
        this.ruleService.handleSaveRule(currentRule);
        return "redirect:/admin/homestay-infor/rule";
    }
}
