package com.gucci.alarm_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients(basePackages = "com.gucci.alarm_service.client")
@SpringBootApplication
public class AlarmServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlarmServiceApplication.class, args);
	}

}
