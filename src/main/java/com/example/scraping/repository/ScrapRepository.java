package com.example.scraping.repository;

import com.example.scraping.domain.scrap.ScrapEntity;
import com.example.scraping.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapRepository extends JpaRepository <ScrapEntity,Long> {
    @Override
    Optional<ScrapEntity> findById(Long id);

    boolean existsByHrefAndUser(String href, User user);
}
