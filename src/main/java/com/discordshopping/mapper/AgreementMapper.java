package com.discordshopping.mapper;

import com.discordshopping.entity.Agreement;
import com.discordshopping.entity.dto.AgreementDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AgreementMapper {

    @Mapping(source = "userAccount.user.nickName", target = "nickName")
    @Mapping(source = "product.productType", target = "productName")
    @Mapping(source = "agreementStatus", target = "status")
    @Mapping(source = "createdAt", target = "date")
    AgreementDto agreementToDto(Agreement agreement);

    List<AgreementDto> agreementToDto(List<Agreement> all);
}
