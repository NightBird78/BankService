package com.discordshopping.service;

import com.discordshopping.entity.Agreement;

import java.util.Optional;

public interface AgreementService {
    Optional<Agreement> findById(String id);
}
