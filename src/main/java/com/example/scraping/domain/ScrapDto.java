package com.example.scraping.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScrapDto {
    private String title;
    private String company;
    private String saraminurl;
    private String deadline;
    private String location;
    private String experience;
    private String requirement;
    private String jobtype;

}
