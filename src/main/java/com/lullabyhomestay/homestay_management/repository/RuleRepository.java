package com.lullabyhomestay.homestay_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.Rule;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {

    public List<Rule> findAllByOrderByIsFixedDesc();
    Optional<Rule> findByRuleID(long ruleID);

    Rule save(Rule rule);

    void deleteByRuleID(long id);
    
}
