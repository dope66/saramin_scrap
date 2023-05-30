package com.example.scraping.domain.scrap;

import com.example.scraping.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@Data
@Table(name = "scrap")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
