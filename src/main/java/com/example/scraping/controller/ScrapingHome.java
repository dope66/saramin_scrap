package com.example.scraping.controller;

import com.example.scraping.domain.user.UserDto;
import com.example.scraping.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ScrapingHome {
    private final UserService userService;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("username",userService.getUsername());
        return "home";
    }
}
