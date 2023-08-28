package com;


import com.bilibili.websocket.WebSocketService;
import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = "com.bilibili")
@EnableMBeanExport
public class BilibiliApp {
    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(BilibiliApp.class, args);
        WebSocketService.setApplicationContext(app);
    }


}
