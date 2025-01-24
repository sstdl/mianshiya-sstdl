package com.sstdl.mianshiya;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.sstdl.mianshiya.mapper")
public class MianshiyaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MianshiyaBackendApplication.class, args);
    }

}
