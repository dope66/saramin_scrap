package com.example.scraping.controller;

import com.example.scraping.domain.scrap.ScrapEntity;
import com.example.scraping.domain.user.User;
import com.example.scraping.domain.user.UserDto;
import com.example.scraping.repository.UserRepository;
import com.example.scraping.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final UserRepository userRepository;

    @GetMapping("/join")
    public String showjoin(Model model) {
        model.addAttribute("userDto",new UserDto());
        return "join";
    }
    @PostMapping("/joinProc")
    public String dojoin(UserDto userDto, Model model ){
        User newUser = userService.dojoin(userDto);
        System.out.println("유저 아이디 생성 성공");
        return "redirect:/";
    }

    @GetMapping("/login")
    public String showlogin() {
        return "login";
    }
    @PostMapping("/login")
    public String doLogin(){
        return "login";
    }

    @GetMapping("/logout")
    public String logout(){
        return "logout";
    }
    @GetMapping("/page")
    public String userpage(Model model, Principal principal){
        String username = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            List<ScrapEntity> myScraps = user.getScraps();
            model.addAttribute("user", user);
            model.addAttribute("myScraps", myScraps);
            return "userPage";
        }else{
            return "login";
        }
    }
}
