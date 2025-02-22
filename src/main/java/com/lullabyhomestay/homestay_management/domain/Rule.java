package com.lullabyhomestay.homestay_management.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Rules")
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RuleID")
    private long ruleID;

    @Column(name = "RuleTitle")
    @NotBlank(message = "Vui lòng nhập tiêu đề quy tắc")
    private String ruleTitle;

    @Column(name = "Description")
    private String description;

    @Column(name = "icon")
    private String icon;

    @Column(name = "IsHidden")
    private Boolean isHidden;
}
