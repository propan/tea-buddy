package com.github.propan.teabuddy;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import de.neuland.pug4j.PugConfiguration;
import de.neuland.pug4j.spring.template.SpringTemplateLoader;
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

    @Bean
    public MailjetClient mailjetClient() {
        ClientOptions options = ClientOptions.builder()
                .apiKey(System.getenv("MAILJET_KEY"))
                .apiSecretKey(System.getenv("MAILJET_SECRET"))
                .build();
        return new MailjetClient(options);
    }

    @Bean
    public SpringTemplateLoader templateLoader() {
        SpringTemplateLoader templateLoader = new SpringTemplateLoader();
        templateLoader.setTemplateLoaderPath("classpath:/templates/");
        return templateLoader;
    }

    @Bean
    public PugConfiguration pugConfiguration() {
        PugConfiguration configuration = new PugConfiguration();
        configuration.setPrettyPrint(true);
        configuration.setCaching(true);
        configuration.setTemplateLoader(templateLoader());
        return configuration;
    }

    public static void main(String[] args) {
        SpringApplication.run(TeaBuddyApplication.class, args);
    }

}
