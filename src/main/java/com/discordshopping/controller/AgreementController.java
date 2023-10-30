package com.discordshopping.controller;

import com.discordshopping.bot.util.ValidUUID;
import com.discordshopping.entity.dto.AgreementDto;
import com.discordshopping.service.AgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("agreement")
@RequiredArgsConstructor
public class AgreementController {

    private final AgreementService agreementService;

    @GetMapping("/get/{id}")
    public AgreementDto getAgreement(@ValidUUID @PathVariable("id") String id) {
        return agreementService.findDtoById(id);
    }
}