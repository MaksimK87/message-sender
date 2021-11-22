package net.qmate.sender.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@EnableTransactionManagement
@Configuration
@EnableScheduling
public class AppConfig {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
