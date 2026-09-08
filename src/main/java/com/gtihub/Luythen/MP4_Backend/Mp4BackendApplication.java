package com.gtihub.Luythen.MP4_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Mp4BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(Mp4BackendApplication.class, args);
	}

}
