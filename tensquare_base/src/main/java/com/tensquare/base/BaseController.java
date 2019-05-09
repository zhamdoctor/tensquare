package com.tensquare.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

@SpringBootApplication
public class BaseController {
    public static void main(String[] args) {
        SpringApplication.run(BaseController.class);
    }
    @Bean//添加这个注解把idworker放入容器(SERVLET容器)
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }
}
