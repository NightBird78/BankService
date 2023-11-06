package com.discordshopping.service;

import com.discordshopping.entity.Agreement;
import com.discordshopping.entity.dto.AgreementDto;

import java.util.List;

public interface AgreementService {
    Agreement findById(String id);

    AgreementDto findDtoById(String id);

    List<AgreementDto> findAllDtoById(String id);
}
