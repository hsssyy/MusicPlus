package com.he.musicplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.he.musicplus.mapper")
public class MusicplusApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicplusApplication.class, args);
    }

}
