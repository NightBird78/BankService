package com.discordshopping.service;

import com.discordshopping.entity.Agreement;
import com.discordshopping.dto.AgreementCreatedDto;
import com.discordshopping.dto.AgreementDto;
import com.discordshopping.dto.AgreementFullDto;

import java.util.List;

public interface AgreementService {
    Agreement findById(String id);

    AgreementDto findDtoById(String id);

    List<AgreementDto> findAllDtoById(String id);

    AgreementDto create(AgreementCreatedDto createdDto);

    AgreementFullDto findFullDtoById(String id);
}
