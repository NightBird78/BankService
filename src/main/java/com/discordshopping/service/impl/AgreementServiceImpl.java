package com.discordshopping.service.impl;

import com.discordshopping.entity.Agreement;
import com.discordshopping.entity.Product;
import com.discordshopping.entity.UserAccount;
import com.discordshopping.dto.AgreementCreatedDto;
import com.discordshopping.dto.AgreementDto;
import com.discordshopping.entity.enums.AgreementStatus;
import com.discordshopping.entity.enums.CurrencyCode;
import com.discordshopping.entity.enums.TransactionType;
import com.discordshopping.exception.NotFoundException;
import com.discordshopping.exception.enums.ErrorMessage;
import com.discordshopping.mapper.AgreementMapper;
import com.discordshopping.service.AccountService;
import com.discordshopping.service.AgreementService;
import com.discordshopping.service.ProductService;
import com.discordshopping.service.repository.AgreementRepository;
import com.discordshopping.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    public AgreementDto create(AgreementCreatedDto createdDto) {

        Product product = productService.findById(createdDto.getProductId());
        UserAccount account = accountService.findById(createdDto.getAccountId());

        Agreement agreement = new Agreement();

        System.out.println(agreement);

        agreement.setProduct(product);
        agreement.setUserAccount(account);

        agreement.setCurrencyCode(CurrencyCode.valueOf(createdDto.getCurrencyCode()));
        agreement.setAgreementStatus(AgreementStatus.Active);
        agreement.setAgreementLimit(account.getUser().getEarning().multiply(BigDecimal.valueOf(10)));

        agreement.setOriginalSum(BigDecimal.valueOf(Double.parseDouble(createdDto.getSum())));
        agreement.setDiscountRate(Util.check(Double.parseDouble(createdDto.getSum())));
        agreement.setInterestRate(product.getInterestRate() - agreement.getDiscountRate());
        agreement.setSum(agreement.getOriginalSum());
        agreement.setPaidSum(BigDecimal.ZERO);

        accountService.transfer(
                "IDBA0000000000000000",
                account.getIdba(),
                agreement.getCurrencyCode().toString(),
                agreement.getOriginalSum().toString(),
                product.getProductType().toString(),
                TransactionType.AgreementTransactions.toString()
        );

        return agreementMapper.agreementToDto(agreementRepository.save(agreement));
    }
}