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

    public List<ScrapDto> scrapeJobs(String keyword, Integer allpage, String career) {
        List<ScrapDto> jobs = new ArrayList<>();
        for (int page = 1; page <= allpage; page++) {
            String url = buildUrl(keyword,page,career);
            try {
//                연결 시도
                Connection connection = Jsoup.connect(url)
                        .header("User-Agent", "Mozilla/5.0")
                        .timeout(10000);
                Document doc = connection.get();
                Elements jobElements = doc.select("div#recruit_info_list > div.content > div.item_recruit");

                for (Element jobElement : jobElements) {
                    ScrapDto scrapDto = parseJobElement(jobElement);
                    jobs.add(scrapDto);
                }
            } catch (IOException e) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return jobs;
    }
    /*
    * build url
    * 1  = 신입
    * 2 = 경력
    * 3 = 전체
    * */
    private String buildUrl(String keyword , int page, String career){
        String baseUrl = "https://www.saramin.co.kr/zf_user/search/recruit";
        String url = null;
        if (career.equals("1")) {
            url =  baseUrl + "?searchType=search&searchword=" +
                    keyword + "&company_cd=0%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C9%2C10&exp_cd=1&panel_type=&search_optional_item=y&search_done=y&panel_count=y&preview=y&recruitPage=" +
                    page + "&recruitSort=relation&recruitPageCount=10";
        } else if (career.equals("2")) {
            url = baseUrl + "?searchType=search&searchword=" +
                    keyword + "&company_cd=0%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C9%2C10&exp_cd=2&panel_type=&search_optional_item=y&search_done=y&panel_count=y&preview=y&recruitPage=" +
                    page + "&recruitSort=relation&recruitPageCount=10";
        } else if (career.equals("3")) {
            url = baseUrl + "?search_area=main&search_done=y&search_optional_item=n&searchType=recently&searchword=" +
                    keyword + "&recruitPage=" + page + "&recruitSort=relation&recruitPageCount=10";
        }
        return url;
    }
    /*
     * 크롤링 데이터 관리
     * */
    public ScrapDto parseJobElement(Element jobElement) {
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

        return new ScrapDto(title, href, company, deadline, location, experience, requirement, jobtype);
    }
}


