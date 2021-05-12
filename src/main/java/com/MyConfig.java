package com;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class MyConfig {

    @Bean
    public SnsClient usEast2SnsClient() {
        return SnsClient.builder()
                .region(Region.US_EAST_2)
                .build();
    }

    @Bean
    public SqsClient usEast2SqsClient() {
        return SqsClient.builder()
                .region(Region.US_EAST_2)
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
