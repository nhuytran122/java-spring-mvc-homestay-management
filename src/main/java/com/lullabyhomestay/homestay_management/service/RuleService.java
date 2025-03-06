package com.lullabyhomestay.homestay_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.Rule;
import com.lullabyhomestay.homestay_management.exception.NotFoundException;
import com.lullabyhomestay.homestay_management.repository.RuleRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RuleService {
    public final RuleRepository ruleRepository;

    public List<Rule> getAllRules() {
        return this.ruleRepository.findAll();
    }

    public List<Rule> getRulesByIsHidden(Boolean isHidden) {
        return this.ruleRepository.findByIsHidden(isHidden);
    }

    public void handleSaveRule(Rule rule) {
        this.ruleRepository.save(rule);
    }

    public Rule getRuleByRuleID(long ruleID) {
        Optional<Rule> ruleOpt = ruleRepository.findByRuleID(ruleID);
        if (!ruleOpt.isPresent()) {
            throw new NotFoundException("Quy tắc");
        }
        return ruleOpt.get();
    }
}
