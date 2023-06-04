package com.example.scraping.controller;

import com.example.scraping.domain.scrap.ScrapEntity;
import com.example.scraping.domain.user.User;
import com.example.scraping.domain.user.UserDto;
import com.example.scraping.repository.ScrapRepository;
import com.example.scraping.repository.UserRepository;
import com.example.scraping.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr")
public class UserController {
    @Autowired
    private final UserService userService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    @GetMapping("/join")
    public String showjoin(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "join";
    }

    @PostMapping("/joinProc")
    public String dojoin(UserDto userDto, Model model) {
        try{
            User newUser = userService.dojoin(userDto);
            System.out.println("유저 아이디 생성 성공");
            model.addAttribute("successMessage", "회원 가입이 성공적으로 완료되었습니다.");
            return "redirect:/";
        }catch (IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());
            model.addAttribute("userDto",userDto);
            model.addAttribute("showErrorMessage", true);
            return "join";
        }
    }

    @GetMapping("/login")
    public String showlogin() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }

    @GetMapping("/page")
    public String userpage(Model model) {
        String username = userService.getUsername();
        User user = userService.getUserByUsername(username);
        if (user != null) {
            List<ScrapEntity> scraps = user.getScraps();
            model.addAttribute("user", user);
            model.addAttribute("scraps", scraps);
            return "userPage";
        } else {
            return "login";
        }
    }
    @PostMapping("/deleteScrap")
    public String deleteScrap(Long scrapId){
        userService.deleteScrap(scrapId);
        return "redirect:/usr/page";

    }
    @PostMapping("/deleteUser")
    public String deleteUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        userService.deleteUserByUsername(username);
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/usr/page";
    }

}
