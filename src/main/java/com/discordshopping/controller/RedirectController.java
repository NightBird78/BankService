package com.discordshopping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

    @GetMapping("/swagger")
    public String redirectToSwagger() {
        return "redirect:/swagger-ui/index.html";
    }

    @GetMapping("/")
    public String redirectToH() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String redirectToHome() {
        return "site/home";
    }

    @GetMapping("/about/me")
    public String redirectToAboutMe() {
        return "site/about1";
    }

    @GetMapping("/about/project")
    public String redirectToAboutProject() {
        return "site/about2";
    }

    @GetMapping("/about/project-result")
    public String redirectToAboutProject_Result() {
        return "site/about3";
    }

    @GetMapping("/login")
    public String redirectToLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String redirectToRegister() {
        return "register";
    }

    @GetMapping("/cabinet")
    public String redirectToCabinet() {
        return "secure/cabinet";
    }
}
