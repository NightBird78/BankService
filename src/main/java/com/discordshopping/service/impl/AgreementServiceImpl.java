package com.discordshopping.service.impl;

import com.discordshopping.entity.Agreement;
import com.discordshopping.entity.Product;
import com.discordshopping.entity.Transaction;
import com.discordshopping.entity.UserAccount;
import com.discordshopping.entity.dto.AgreementCreatedDto;
import com.discordshopping.entity.dto.AgreementDto;
import com.discordshopping.entity.dto.AgreementFullDto;
import com.discordshopping.entity.dto.TransactionDto;
import com.discordshopping.entity.enums.AgreementStatus;
import com.discordshopping.entity.enums.CurrencyCode;
import com.discordshopping.entity.enums.TransactionType;
import com.discordshopping.exception.NotFoundException;
import com.discordshopping.exception.enums.ErrorMessage;
import com.discordshopping.mapper.AgreementMapper;
import com.discordshopping.service.AccountService;
import com.discordshopping.service.AgreementService;
import com.discordshopping.service.ProductService;
import com.discordshopping.service.TransactionService;
import com.discordshopping.service.repository.AgreementRepository;
import com.discordshopping.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgreementServiceImpl implements AgreementService {

    private final AgreementRepository agreementRepository;
    private final AgreementMapper agreementMapper;
    private final ProductService productService;
    private final AccountService accountService;

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
        return agreementMapper.agreementToDto(agreementRepository.findAllByAccountId(UUID.fromString(id)));
    }

    @Override
    @Transactional
    public ResponseEntity<List<Object>> create(AgreementCreatedDto createdDto) {

        Product product = productService.findById(createdDto.getProductId());
        UserAccount account = accountService.findById(createdDto.getAccountId());

        Agreement agreement = new Agreement();

        agreement.setProduct(product);
        agreement.setUserAccount(account);

        agreement.setCurrencyCode(CurrencyCode.valueOf(createdDto.getCurrencyCode()));
        agreement.setAgreementStatus(AgreementStatus.Active);
        agreement.setAgreementLimit(account.getUser().getEarning() * 10);

        agreement.setOriginalSum(Double.parseDouble(createdDto.getSum()));
        agreement.setDiscountRate(Util.check(Double.parseDouble(createdDto.getSum())));
        agreement.setInterestRate(product.getInterestRate() - agreement.getDiscountRate());
        agreement.setSum(agreement.getOriginalSum());
        agreement.setPaidSum(0.0);

        agreementRepository.save(agreement);

        TransactionDto tr = accountService.transfer(
                "IDBA0000000000000000",
                account.getIdba(),
                agreement.getCurrencyCode().toString(),
                agreement.getOriginalSum().toString(),
                product.getProductType().toString(),
                TransactionType.AgreementTransactions.toString()
        );

        return ResponseEntity.ok(
                List.of(
                        agreementMapper.agreementToDto(agreement),
                        tr
                )
        );

    }

    @Override
    public AgreementFullDto findFullDtoById(String id) {
        return null;
    }
}