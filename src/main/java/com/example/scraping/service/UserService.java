package com.example.scraping.service;

import com.example.scraping.domain.user.User;
import com.example.scraping.domain.user.UserDto;
import com.example.scraping.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public User dojoin(UserDto userDto){
        return userRepository.save(userDto.toEntity());

    }




}
