package com.example.scraping.service;

import com.example.scraping.domain.scrap.ScrapDto;
import com.example.scraping.domain.scrap.ScrapEntity;
import com.example.scraping.domain.user.User;
import com.example.scraping.repository.ScrapRepository;
import com.example.scraping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScrapingService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ScrapRepository scrapRepository;

    public List<ScrapDto> scrapeJobs(String keyword, Integer allpage, String career) {
        List<ScrapDto> jobs = new ArrayList<>();
        for (int page = 1; page <= allpage; page++) {
            String url = buildUrl(keyword, page, career);
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
     * default = 3
     * */
    private String buildUrl(String keyword, int page, String career) {
        String baseUrl = "https://www.saramin.co.kr/zf_user/search/recruit";
        String url = null;
        if (career.equals("1")) {
            url = baseUrl + "?searchType=search&searchword=" +
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

        Element titleElement = jobElement.selectFirst("div.area_job > h2.job_tit > a");
        String title = titleElement.text();

        Element companyElement = jobElement.selectFirst("div.area_corp > strong.corp_name > a");
        String company = (companyElement != null) ? companyElement.text() : "N/A";

        Element linkElement = titleElement;
        linkElement.attr("target", "_blank"); // target="_blank" 추가
        String href = "https://www.saramin.co.kr" + linkElement.attr("href");


        Element deadlineElement = jobElement.selectFirst("div.area_job > div.job_date > span.date");
        String deadline = deadlineElement.text();
        Elements locationElements = jobElement.select("div.job_condition > span");
        String location = getElementText(locationElements, 0);
        String experience = getElementText(locationElements, 1);
        String requirement = getElementText(locationElements, 2);
        String jobtype = getElementText(locationElements, 3);
//        직무
//        Elements job_sectorElement  = jobElement.select("div.item_recruit > div.area_job > div.job_sector > a ");
//        String job_sector=(job_sectorElement).text();

        return new ScrapDto(title, href, company, deadline, location, experience, requirement, jobtype);
    }



    public String saveScrap(String title, String href, String company, String deadline, String location, String experience, String requirement, String jobtype, String username) {

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // 중복 스크랩 체크
            if (scrapRepository.existsByHrefAndUser(href, user)) {
                // 이미 스크랩된 경우 처리 로직
                return "redirect:/scraping?duplicate";
            }
            // id 값을 초기화 시키지 않아 기본값이 들어가게된다.
            ScrapEntity scrapEntity = new ScrapEntity(null, title, href, company, deadline, location, experience, requirement, jobtype, user);
            scrapRepository.save(scrapEntity);


        }

        return "redirect:/";

    }

    private String getElementText(Elements elements, int index) {
        return (index >= 0 && index < elements.size()) ? elements.get(index).text() : null;
    }
}


