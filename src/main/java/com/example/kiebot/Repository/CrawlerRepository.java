package com.example.kiebot.Repository;

import com.example.kiebot.dto.CrawlerDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@Repository

public interface CrawlerRepository extends JpaRepository<CrawlerDto,Long>{

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM CRAWLER_DTO WHERE TO_DATE(date_time, 'YYYY.MM.DD') < CURRENT_DATE - INTERVAL '10' DAY AND ANNOUNCEMENTS != '공지'", nativeQuery = true)
    void deleteByDateTimeBefore();
    CrawlerDto findByDataId(int dataId);


}
