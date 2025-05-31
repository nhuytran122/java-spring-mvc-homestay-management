package com.lullabyhomestay.homestay_management.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lullabyhomestay.homestay_management.domain.FAQ;

@Repository
public interface FAQRepository extends JpaRepository<FAQ, Long> {

    Page<FAQ> findAll(Pageable page);

    Page<FAQ> findByQuestionContainingIgnoreCaseOrAnswerContainingIgnoreCase(
            String ques, String ans, Pageable pageable);

    Optional<FAQ> findByFaqId(long faqId);

    FAQ save(FAQ faq);

    void deleteByFaqId(long id);
}