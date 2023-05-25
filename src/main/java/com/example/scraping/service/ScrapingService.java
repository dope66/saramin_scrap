package com.example.scraping.service;

import com.example.scraping.domain.ScrapDto;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScrapingService {

    public List<ScrapDto> scrapeJobs(String keyword, Integer allpage) {
        List<ScrapDto> jobs = new ArrayList<>();
        for (int page = 1; page <= allpage; page++) {
            String url = "https://www.saramin.co.kr/zf_user/search/recruit?search_area=main&search_done=y&search_optional_item=n&searchType=recently&searchword=" + keyword + "&recruitPage=" + page + "&recruitSort=relation&recruitPageCount=100";
            try {
                Connection connection = Jsoup.connect(url).header("User-Agent", "Mozilla/5.0");
                Document doc = connection.get();
                Elements jobElements = doc.select("div#recruit_info_list > div.content > div.item_recruit");
                for (Element jobElement : jobElements) {
                    Element titleElement = jobElement.select("div.item_recruit >div.area_job > h2.job_tit > a").first();
                    String title = titleElement.text();
                    Element companyElement = jobElement.select("div.item_recruit > div.area_corp > strong.corp_name > a").first();
                    String company = (companyElement != null) ? companyElement.text() : "N/A"; // null일 경우 대체값을 지정합니다.
                    Element linkElement = jobElement.select("div.item_recruit> div.area_job > h2.job_tit > a ").first();
                    String href = "https://www.saramin.co.kr" + linkElement.attr("href");
                    Element deadlineElement = jobElement.select("div.item_recruit > div.area_job > div.job_date > span.date").first();
                    String deadline = deadlineElement.text();
                    Elements locationElements = jobElement.select("div.job_condition > span");
                    String location = null;
                    String experience = null;
                    String requirement = null;
                    String jobtype = null;
                    for (int i = 0; i < locationElements.size(); i++) {
                        Element locationElement = locationElements.get(i);
                        // i 변수를 기준으로 원하는 값을 가져옵니다.
                        if (i == 0) {
                            location = locationElement.text();
                        } else if (i == 1) {
                            experience = locationElement.text();
                        } else if (i == 2) {
                            requirement = locationElement.text();
                        } else if (i == 3) {
                            jobtype = locationElement.text();
                        }
                    }

                    ScrapDto scrapDto = new ScrapDto(title, href, company, deadline, location, experience, requirement, jobtype);
                    jobs.add(scrapDto);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jobs;
    }
}


