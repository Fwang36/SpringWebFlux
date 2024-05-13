package com.example.hello;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import io.sentry.spring.jakarta.tracing.SentryTransaction;

@Component
public class ScheduledTaskComponent {

    private final ExternalApiClient externalApiClient;

    @Autowired
    public ScheduledTaskComponent(ExternalApiClient externalApiClient) {
        this.externalApiClient = externalApiClient;
    }
    @Scheduled(fixedRate = 5000)
    @SentryTransaction(operation = "scheduledTask")
    public void performScheduledTask() {
        System.out.println("Performing scheduled task");
        externalApiClient.fetchData()
                         .subscribe(response -> System.out.println("Received from API: " + response),
                                    error -> System.err.println("Error during API call: " + error.getMessage()));
    }
}
