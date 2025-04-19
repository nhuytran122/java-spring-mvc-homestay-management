package com.lullabyhomestay.homestay_management.service;

import org.springframework.stereotype.Service;

import com.lullabyhomestay.homestay_management.domain.ActionLog;
import com.lullabyhomestay.homestay_management.repository.ActionLogRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ActionLogService {
    private final ActionLogRepository actionLogRepo;

    public ActionLog handleSaveLog(ActionLog actionLog) {
        return actionLogRepo.save(actionLog);
    }
}
