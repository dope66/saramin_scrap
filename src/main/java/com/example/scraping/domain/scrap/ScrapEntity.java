package com.example.scraping.domain.scrap;

import com.example.scraping.domain.user.User;
import jakarta.persistence.*;

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
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;


}
