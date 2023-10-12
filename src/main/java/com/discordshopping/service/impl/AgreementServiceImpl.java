package com.discordshopping.service.impl;

import com.discordshopping.entity.Agreement;
import com.discordshopping.service.AgreementService;
import com.discordshopping.service.repository.AgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgreementServiceImpl implements AgreementService {

    private final AgreementRepository agreementRepository;

    @Override
    public Optional<Agreement> findById(String id) {
        return agreementRepository.findById(UUID.fromString(id));
    }
}