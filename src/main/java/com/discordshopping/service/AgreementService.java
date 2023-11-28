package com.discordshopping.service;

import com.discordshopping.entity.Agreement;
import com.discordshopping.entity.dto.AgreementCreatedDto;
import com.discordshopping.entity.dto.AgreementDto;
import com.discordshopping.entity.dto.AgreementFullDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AgreementService {
    Agreement findById(String id);

    AgreementDto findDtoById(String id);

    List<AgreementDto> findAllDtoById(String id);

    ResponseEntity<List<Object>> create(AgreementCreatedDto createdDto);

    AgreementFullDto findFullDtoById(String id);
}
