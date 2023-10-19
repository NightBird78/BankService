package com.discordshopping.service;

import com.discordshopping.entity.Agreement;
import com.discordshopping.entity.dto.AgreementDto;

public interface AgreementService {
    Agreement findById(String id);

    AgreementDto findDtoById(String id);
}
