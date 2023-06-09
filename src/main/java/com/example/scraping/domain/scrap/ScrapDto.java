package com.example.scraping.domain.scrap;

import com.example.scraping.domain.user.User;
import com.example.scraping.domain.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScrapDto {
    private String title;
    private String href;
    private String company;
    private String deadline;
    private String location;
    private String experience;
    private String requirement;
    private String jobtype;

}
