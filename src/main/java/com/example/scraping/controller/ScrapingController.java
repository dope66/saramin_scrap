package com.example.scraping.controller;

import com.example.scraping.domain.scrap.ScrapDto;
import com.example.scraping.service.ScrapingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class ScrapingController {

    @Autowired
    private final ScrapingService scrapingService;

    @GetMapping("/scraping")
    public String scraping(@RequestParam(value = "keyword", required = false) String keyword,
                           @RequestParam(value = "allpage", required = false) Integer allpage,
                           @RequestParam(value="career" ,defaultValue="3") String career,
                           Model model,
                           RedirectAttributes redirectAttributes
                           ) {
        if (keyword == null || allpage == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "키워드와 페이지 수를 입력해야 합니다.");
            return "redirect:/";
        }

        List<ScrapDto> jobs = scrapingService.scrapeJobs(keyword, allpage,career);
        model.addAttribute("jobs", jobs);
        return "scraping";
    }


    @PostMapping("/scraping/save")
    public String saveScrap(@RequestParam("title") String title,
                            @RequestParam("href") String href,
                            @RequestParam("company") String company,
                            @RequestParam("deadline") String deadline,
                            @RequestParam("location") String location,
                            @RequestParam("experience") String experience,
                            @RequestParam("requirement") String requirement,
                            @RequestParam("jobtype") String jobtype,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes
                           ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/usr/login"; // 로그인 화면으로 리다이렉트
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        String result = scrapingService.saveScrap(title, href, company, deadline, location, experience, requirement, jobtype, username);
        if (result.equals("duplicate")) {
            redirectAttributes.addAttribute("error", "duplicate");
        }

        return "redirect:/";
    }



}
