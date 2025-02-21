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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "FAQs")
public class FAQ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FAQID")
    private long faqID;

    @NotBlank(message = "Vui lòng nhập câu hỏi")
    @Column(name = "Question")
    private String question;

    @NotBlank(message = "Vui lòng nhập câu trả lời")
    @Column(name = "Answer")
    private String answer;
}

