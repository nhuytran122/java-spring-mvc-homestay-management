package com.lullabyhomestay.homestay_management.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lullabyhomestay.homestay_management.domain.FAQ;
import com.lullabyhomestay.homestay_management.repository.FAQRepository;
import com.lullabyhomestay.homestay_management.utils.Constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class FAQService {
    private final FAQRepository faqRepository;

    public Page<FAQ> getAllFAQs(Pageable pageable) {
        return this.faqRepository.findAll(pageable);
    }

    public Page<FAQ> searchFAQs(String keyword, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.PAGE_SIZE);
        return (keyword != null && !keyword.isEmpty())
                ? faqRepository.findByQuestionContainingIgnoreCaseOrAnswerContainingIgnoreCase(
                        keyword, keyword, pageable)
                : faqRepository
                        .findAll(pageable);
    }

    public void handleSaveFAQ(FAQ faq) {
        this.faqRepository.save(faq);
    }

    public Optional<FAQ> getFAQByFAQID(long faqID) {
        return this.faqRepository.findByFaqID(faqID);
    }

    @Transactional
    public void deleteByFAQID(long faqID) {
        this.faqRepository.deleteByFaqID(faqID);
    }
}
