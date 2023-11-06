package com.discordshopping.service.impl;

import com.discordshopping.entity.Agreement;
import com.discordshopping.entity.dto.AgreementDto;
import com.discordshopping.exception.NotFoundException;
import com.discordshopping.exception.enums.ErrorMessage;
import com.discordshopping.mapper.AgreementMapper;
import com.discordshopping.service.AgreementService;
import com.discordshopping.service.repository.AgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgreementServiceImpl implements AgreementService {

    private final AgreementRepository agreementRepository;
    private final AgreementMapper agreementMapper;

    @Override
    public Agreement findById(String id) {
        return agreementRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND));
    }

    @Override
    public AgreementDto findDtoById(String id) {
        return agreementMapper.agreementToDto(findById(id));

    }

    @Override
    public List<AgreementDto> findAllDtoById(String id) {
        return agreementMapper.agreementToDto(agreementRepository.findAllByAccountId(id));
    }
}