package com.mobai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.mobai.controller", "com.mobai.service", "com.mobai.config"})
@MapperScan(basePackages = {"com.mobai.dao"})
public class MysqlBackupsApplication {
    public static void main(String[] args) {
        SpringApplication.run(MysqlBackupsApplication.class, args);
    }
}
