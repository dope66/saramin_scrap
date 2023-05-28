package com.example.scraping.domain.scrap;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ScrapEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String href;
    private String company;
    private String deadline;
    private String location;
    private String experience;
    private String requirement;
    private String jobtype;


}
