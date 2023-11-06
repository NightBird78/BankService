package com.discordshopping.controller;

import com.discordshopping.bot.util.validator.annotation.ValidUUID;
import com.discordshopping.entity.dto.AgreementDto;
import com.discordshopping.service.AgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("agreement")
@RequiredArgsConstructor
public class AgreementController {

    private final AgreementService agreementService;

    @GetMapping("/get/details")
    public AgreementDto getAgreement(@ValidUUID @Param("id") String id) {
        return agreementService.findDtoById(id);
    }

    @GetMapping("/get/all-by-account")
    public List<AgreementDto> getAllAgreement(@ValidUUID @Param("id") String id) {
        return agreementService.findAllDtoById(id);
    }
}