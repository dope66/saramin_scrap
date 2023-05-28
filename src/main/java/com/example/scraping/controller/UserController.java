package com.example.scraping.controller;

import com.example.scraping.domain.user.User;
import com.example.scraping.domain.user.UserDto;
import com.example.scraping.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService userService;
    @GetMapping("/join")
    public String showjoin(Model model) {
        model.addAttribute("userDto",new UserDto());
        return "join";
    }
    @PostMapping("/joinProc")
    public String dojoin(UserDto userDto, Model model ){
        User newUser = userService.dojoin(userDto);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String showlogin() {
        return "login";
    }

    @PostMapping("/login")
    public String dologin() {
        return "login";
    }


}
