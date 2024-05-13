package com.example.hello;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class ExternalApiClient {

    private final WebClient webClient;

    public ExternalApiClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://127.0.0.1:8000/").build();
    }
    
    public Mono<String> fetchData() {
        return this.webClient.get()
            .uri("/test")
            .retrieve()
            .bodyToMono(String.class);
    }
}