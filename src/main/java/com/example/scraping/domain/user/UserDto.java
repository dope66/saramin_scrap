package com.example.scraping.domain.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private String userId;
    private String password;
    private String userScrap;

    public User toEntity(){

        return User.builder()
                .userId(userId)
                .password(password)
                .build();
    }

}
