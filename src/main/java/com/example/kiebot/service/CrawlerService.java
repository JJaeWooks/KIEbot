package com.example.kiebot.service;

import com.example.kiebot.Repository.CrawlerRepository;
import com.example.kiebot.dto.CrawlerDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


@Service
@AllArgsConstructor
@NoArgsConstructor
public class CrawlerService {
    @Autowired
    CrawlerRepository crawlerRepository;

    private static String IsoAnnouncementsUrl="https://www.gnu.ac.kr/ise/na/ntt/selectNttList.do?mi=2998&bbsId=1368"; //공지사항 주소
    private static String IsoAnnouncementsUrl2="https://www.gnu.ac.kr/ise/na/ntt/selectNttInfo.do?mi=2998&bbsId=1368"; //공지사항 주소
    private static String IsoParticipationProgram="https://www.gnu.ac.kr/ise/na/ntt/selectNttList.do?mi=3001&bbsId=1373"; //참여프로그램 주소
    private static String IsoParticipationProgram2="https://www.gnu.ac.kr/ise/na/ntt/selectNttInfo.do?mi=3001&bbsId=1373"; //참여프로그램 주소
    private static String IsoScholarshipInformation="https://www.gnu.ac.kr/ise/na/ntt/selectNttList.do?mi=3003&bbsId=1374"; //장학정보 주소
    private static String IsoScholarshipInformation2="https://www.gnu.ac.kr/ise/na/ntt/selectNttInfo.do?mi=3003&bbsId=1374"; //장학정보 주소
    private static String IsoEmploymentInformation="https://www.gnu.ac.kr/ise/na/ntt/selectNttList.do?mi=3006&bbsId=1376";//취업정보
    private static String IsoEmploymentInformation2="https://www.gnu.ac.kr/ise/na/ntt/selectNttInfo.do?mi=3006&bbsId=1376";//취업정보


    @PostConstruct
    public void menu() throws Exception {
        int option = 0;
        Scanner sc = new Scanner(System.in);

        while (option != 1) {
            System.out.println("\n\n\n\n\n\n#########################정보##########################");
            System.out.println("제작자 : 경상대학교 산업시스템공학부(컴퓨터과학과) 19학번 전재욱");
            System.out.println("제작일 : 2023-07-10 03:22");
            System.out.println("문의 : 010-9632-7556 (카톡 : jj99526 인스타 : jj______6");
            System.out.println("도움 : 경상대학교 컴퓨터과학과 구석방");
            System.out.println("######################################################");

            System.out.println("\n###################### KIE BOT #######################");
            System.out.println("옵션을 선택하세요 \n1: 실행\n2: 옛날 데이터베이스 삭제 (등록후 100일 지난 데이터 수동 삭제 <- 자동으로 삭제되게 되어있음)");
            System.out.println("######################################################");
            System.out.print("번호를 입력하세요 : ");

            option = sc.nextInt();

            switch (option) {
                case 1:
                    break;
                case 2:
                    System.out.println("데이터베이스 삭제");
                    delete();
                    break;
                case -1:
                    System.out.println("프로그램을 종료합니다.");
                    break;
                default:
                    System.out.println("잘못된 옵션입니다. 다시 입력하세요.");
            }
        }

    }
    @Scheduled(cron = "0 0 0 */100 * *") //100일 마다 데이터중 작성일로부터 100일 지난 데이터 삭제
    public void delete(){
      crawlerRepository.deleteByDateTimeBefore();
    }

    @Scheduled(fixedRate = 500000) //50분마다 크롤링 하기
    public void getAnnouncements() throws Exception {
    //크롤링 코드
        Document doc1=Jsoup.connect(IsoAnnouncementsUrl).get();
        Document doc2=Jsoup.connect(IsoParticipationProgram).get();
        Document doc3=Jsoup.connect(IsoScholarshipInformation).get();
        Document doc4=Jsoup.connect(IsoEmploymentInformation).get();

        //table tbody tr부분을 크롤링 세부내용은 아래 반복문에서 추가설정
        Elements EDoc1 = doc1.select("table tbody tr");
        Elements EDoc2 = doc2.select("table tbody tr");
        Elements EDoc3 = doc3.select("table tbody tr");
        Elements EDoc4 = doc4.select("table tbody tr");

        //주소들을 arraylist형식으로 담아넣음
        String[] listlink={IsoAnnouncementsUrl2,IsoParticipationProgram2,IsoScholarshipInformation2,IsoEmploymentInformation2};
        ArrayList<Elements> elementsList = new ArrayList<>();
        elementsList.add(EDoc1);
        elementsList.add(EDoc2);
        elementsList.add(EDoc3);
        elementsList.add(EDoc4);


        for(int i=0;i<elementsList.size();i++){
            ArrayList<String> windowName = new ArrayList<>(Arrays.asList("1학년","4학년")); //1~4학년 채팅방에 보내기

            if(i>2){   //취업정보 게시판은 4학년만 보내기 (lelmentslist에 저장되어 있는 형식은 [(0)공지사항,(1)참여프로그램,(2)장학정보,(3)취업정보] 이렇게 되어있따)
                ArrayList<String> windowName2 = new ArrayList<>(Arrays.asList("4학년")); //4학년 채팅방에 보내기
                sendlink(elementsList.get(i), listlink[i],windowName2);
            }else{
                sendlink(elementsList.get(i), listlink[i],windowName);
            }
        }
    }

    public void sendlink(Elements elementsList, String URL,ArrayList<String> windowName) throws Exception {

        for (Element link:elementsList){
            //크롤링 후 생성자 만들기
            int id=Integer.parseInt(link.select("td.ta_l a.nttInfoBtn").attr("data-id"));
            CrawlerDto crawlerDto=CrawlerDto.builder()
                    .dataId(id)
                    .title(link.select(" td.ta_l a.nttInfoBtn").text())
                    .link(URL+"&nttSn="+id)
                    .dateTime(link.select("td").get(3).text())
                    .announcements(link.select("b.btn_S.btn_default").text())
                    .build();
             CrawlerDto target =crawlerRepository.findByDataId(crawlerDto.getDataId());
            Thread.sleep(500); //정지시간을 둬서 카카오톡 너무빠르게 보내는걸 방지
             if (target==null){
                 System.out.println("데이터베이스에 값이 없습니다");
                 User32.sendMessage("RICHEDIT50W", windowName, crawlerDto.getTitle()+"\n"+crawlerDto.getLink()+"\n"+"작성일 : "+crawlerDto.getDateTime()); //카톡보내기 호출
                 crawlerRepository.save(crawlerDto); //카톡을 정상적으로 보냈을시 데이터베이스 저장
                 System.out.println(crawlerDto.getDataId()+" "+crawlerDto.getLink()+" "+crawlerDto.getTitle()+" "+crawlerDto.getDateTime());
                   }else{
                 System.out.println("데베에 값이 있음");
             }
       }

    }
}
