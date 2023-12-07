package com.discordshopping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

    @GetMapping("/swagger")
    public String redirectToSwagger() {
        return "redirect:/swagger-ui/index.html";
    }

    @GetMapping({"/", "/home"})
    public String redirectToHome() {
        return "site/home";
    }

    @GetMapping("/secure")
    public String redirectToSecured() {
        return "secure/secured";
    }

    @GetMapping("/about/me")
    public String redirectToAboutMe() {
        return "site/about1";
    }

    @GetMapping("/about/project")
    public String redirectToAboutProject() {
        return "site/about2";
    }
    @GetMapping("/style")
    public String redirectToBootstrap() {
        return "site/bootstrap";
    }

    @GetMapping("/login")
    public String redirectToLogin() {return "login";}
}
