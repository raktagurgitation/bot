package com.program.bot;

import com.program.bot.config.BotConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@EnableConfigurationProperties(BotConfig.class)
@SpringBootApplication
@EnableScheduling
public class BotApplication {

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	@Bean
	public ScheduledExecutorService scheduledExecutorService(){
		return new ScheduledThreadPoolExecutor(4);
	}

	public static void main(String[] args) {
		SpringApplication.run(BotApplication.class, args);
	}

}
