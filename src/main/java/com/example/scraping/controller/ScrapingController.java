package com.example.scraping.controller;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class ScrapingController {
    @GetMapping("/scraping")
    public String scraping(@RequestParam("keyword") String keyword,
                           @RequestParam("page") String page, Model model){
        String url = "https://www.saramin.co.kr/zf_user/search/recruit?search_area=main&search_done=y&search_optional_item=n" +
                "&searchType=search&searchword="+keyword+"&recruitPage="+page+"&recruitSort=relation&recruitPageCount=100";


        //document.querySelector("#recruit_info_list > div.content > div:nth-child(3)")
        try{
            Connection connection = Jsoup.connect(url).header("User-Agent", "Mozilla/5.0");
            Document doc = connection.get();
            Elements jobs = doc.select("div.item_recruit");
          for (Element job:jobs){
              String title = job.selectFirst("div.item_recruit > div.area_job > h2.job_tit > a").text();
              System.out.println("title: "+ title);

              String company = job.selectFirst("div.item_recruit > div.area_corp > strong > a").text();
              System.out.println("회사 이름 : "+company);
              Element linkElement = job.select("div.item_recruit > div.area_job > h2.job_tit > a ").first();
              String href = "https://www.saramin.co.kr" + linkElement.attr("href");
              System.out.println("사람인 주소인데 왜안나올까 "+href);

          }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return "scraping";
    }


}
