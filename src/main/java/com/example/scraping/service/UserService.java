package com.example.scraping.service;

import com.example.scraping.domain.user.User;
import com.example.scraping.domain.user.UserDto;
import com.example.scraping.repository.ScrapRepository;
import com.example.scraping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ScrapRepository scrapRepository;

    public User dojoin(UserDto userDto) {
        //이미 가입된 아이디인지 확인
        Optional<User> existingUser = userRepository.findByUsername(userDto.getUsername());
        if(existingUser.isPresent()){
            throw  new IllegalStateException("이미 가입된 아이디 입니다.");
        }
        return userRepository.save(userDto.toEntity(passwordEncoder));

    }

    public String getUsername() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return authentication.getName();
    }

    public User getUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.orElse(null);
    }
    public void deleteScrap(Long scrapId){
        scrapRepository.deleteById(scrapId);
    }

    public void deleteUserByUsername(String username){
        Optional<User> userOptional  = userRepository.findByUsername(username);
        userOptional.ifPresent(user -> userRepository.delete(user));

    }
}
