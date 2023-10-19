package com.discordshopping.service.impl;

import com.discordshopping.bot.util.MiniUtil;
import com.discordshopping.entity.Agreement;
import com.discordshopping.entity.dto.AgreementDto;
import com.discordshopping.exception.InvalidUUIDException;
import com.discordshopping.exception.NotFoundException;
import com.discordshopping.exception.enums.ErrorMessage;
import com.discordshopping.mapper.AgreementMapper;
import com.discordshopping.service.AgreementService;
import com.discordshopping.service.repository.AgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgreementServiceImpl implements AgreementService {

    private final AgreementRepository agreementRepository;
    private final AgreementMapper agreementMapper;

    @Override
    public Agreement findById(String id) {

        if (!MiniUtil.isValidUUID(id)) {
            throw new InvalidUUIDException(ErrorMessage.INVALID_UUID_FORMAT);
        }

        return agreementRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND));
    }

    @Override
    public AgreementDto findDtoById(String id) {
        return agreementMapper.agreementToDto(findById(id));

    }
}