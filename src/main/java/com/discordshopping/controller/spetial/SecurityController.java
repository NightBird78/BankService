package com.discordshopping.controller.spetial;

import com.discordshopping.validation.annotation.ValidEmail;
import com.discordshopping.validation.annotation.ValidUUID;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/security")
@RestController
public class SecurityController {

    private final Map<String, String> memory = new HashMap<>();

    @GetMapping("/get/email")
    public String getEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping("/set/current/account")
    public void setAccount(@ValidUUID @Param("id") String id) {
        memory.put(getEmail(), id);
    }

    public void clear(@ValidEmail @Param("email") String email) {
        memory.remove(email);
    }
}
