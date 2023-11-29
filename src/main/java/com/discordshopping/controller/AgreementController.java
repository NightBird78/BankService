package com.discordshopping.controller;

import com.discordshopping.dto.AgreementFullDto;
import com.discordshopping.validation.annotation.NullField;
import com.discordshopping.validation.annotation.NumLike;
import com.discordshopping.validation.annotation.ValidUUID;
import com.discordshopping.dto.AgreementCreatedDto;
import com.discordshopping.dto.AgreementDto;
import com.discordshopping.service.AgreementService;
import com.discordshopping.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("agreement")
@RequiredArgsConstructor
public class AgreementController {

    private final AgreementService agreementService;

    @GetMapping("/api/discount/check")
    public ResponseEntity<String> check(@NumLike @Param("sum") String sum) {
        return ResponseEntity.ok(Util.check(sum));
    }

    @GetMapping("/get/detail")
    public AgreementDto getAgreement(@ValidUUID @Param("id") String id) {
        return agreementService.findDtoById(id);
    }

    @GetMapping("/get/detail/more") // todo: mapper, impl, test
    public AgreementFullDto getFullAgreement(@ValidUUID @Param("id") String id) {
        return agreementService.findFullDtoById(id);
    }

    @GetMapping("/get/all/by-account")
    public List<AgreementDto> getAllAgreement(@ValidUUID @Param("id") String id) {
        return agreementService.findAllDtoById(id);
    }

    @RequestMapping(value = "/new", method = {RequestMethod.POST, RequestMethod.GET})
    public AgreementDto createAgreement(@NullField @RequestBody AgreementCreatedDto agreementCreatedDto) {
        return agreementService.create(agreementCreatedDto);
    }
}