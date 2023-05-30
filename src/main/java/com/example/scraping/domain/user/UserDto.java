package com.example.scraping.domain.user;

import com.example.scraping.domain.scrap.ScrapEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDto {
    private String username;
    private String password;


    public User toEntity(PasswordEncoder passwordEncoder){

        return User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();
    }

}
