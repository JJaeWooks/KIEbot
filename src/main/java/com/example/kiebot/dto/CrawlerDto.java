package com.example.kiebot.dto;

import lombok.*;

import javax.persistence.*;
import java.time.format.DateTimeFormatter;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CrawlerDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String link;
    @Column
    private int dataId;
    @Column
    private String dateTime;

    @Column
    private String announcements;

}
