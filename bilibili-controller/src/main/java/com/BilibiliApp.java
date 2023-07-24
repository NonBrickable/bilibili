package com;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.bilibili")

public class BilibiliApp {
    public static void main(String[] args) {
       SpringApplication.run(BilibiliApp.class,args);
    }


}
