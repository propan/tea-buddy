package com.github.propan.teabuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.http.HttpClient;
import java.time.Duration;

@SpringBootApplication
@EnableScheduling
public class TeaBuddyApplication {

    @Bean
    public HttpClient defaultHttpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(TeaBuddyApplication.class, args);
    }

}
