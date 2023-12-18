package com.discordshopping.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/security")
@RestController
public class SecurityController {

    @GetMapping("/get/email")
    public String getEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
