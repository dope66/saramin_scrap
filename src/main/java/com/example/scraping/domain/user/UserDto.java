package com.example.scraping.domain.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@NoArgsConstructor
public class UserDto {
    private String username;
    private String password;
    private String userScrap;

    public User toEntity(PasswordEncoder passwordEncoder){

        return User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();
    }

}
