package com.example.kiebot.controller;

import com.example.kiebot.service.User32;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;

@RestController
public class SendControl {
    //@Scheduled(fixedRate = 5000)
//    public void scheduledSendMessage() {
//        String className = "RICHEDIT50W";
//        ArrayList<String> windowName = new ArrayList<>(Arrays.asList("전재욱", "배민커넥트","카카오메이커스"));
//        String message = "asd";
//        sendMessage(className, windowName, message);
//    }

 //   @GetMapping("/send")
//    public String sendMessage(String className, ArrayList<String> windowName, String a) {
//        User32.sendMessage(className, windowName, a);
//        return "Message sent!";
//    }
}
