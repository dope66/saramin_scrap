package com.example.scraping.controller;

import com.example.scraping.domain.scrap.ScrapDto;
import com.example.scraping.service.ScrapingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ScrapingController {
    @Autowired
    private final ScrapingService scrapingService;

    @GetMapping("/scraping")
    public String scraping(@RequestParam("keyword") String keyword,
                           @RequestParam(value = "allpage", required = false) Integer allpage,
                           @RequestParam(value="career") String career,
                           Model model) {
        List<ScrapDto> jobs = scrapingService.scrapeJobs(keyword, allpage,career);
        model.addAttribute("jobs", jobs);
        return "scraping";
    }
}
