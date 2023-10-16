package com.discordshopping.controller;

import com.discordshopping.bot.util.MiniUtil;
import com.discordshopping.entity.Agreement;
import com.discordshopping.entity.dto.AgreementDto;
import com.discordshopping.exception.InvalidUUIDException;
import com.discordshopping.exception.NotFoundException;
import com.discordshopping.exception.enums.ErrorMessage;
import com.discordshopping.mapper.AgreementMapper;
import com.discordshopping.service.AgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("agreement")
@RequiredArgsConstructor
public class AgreementController {
    private final AgreementService agreementService;
    private final AgreementMapper agreementMapper;

    @GetMapping("/get/{id}")
    public AgreementDto getAgreement(@PathVariable("id") String id){

        if (!MiniUtil.isValidUUID(id)){
            throw new InvalidUUIDException(ErrorMessage.INVALID_UUID_FORMAT);
        }

        Optional<Agreement> opt = agreementService.findById(id);

        if(opt.isPresent()){
            return agreementMapper.agreementToDto(opt.get());
        }
        throw new NotFoundException(ErrorMessage.DATA_NOT_FOUND);
    }
}
