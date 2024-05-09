package com.example.hello;

import reactor.core.publisher.Mono;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GreetingClient {
    private final WebClient client;

    // Spring Boot auto-configures a `WebClient.Builder` instance with nice defaults and customizations.
    // We can use it to create a dedicated `WebClient` for our component.
    public GreetingClient(WebClient.Builder builder) {
      this.client = builder.baseUrl("http://localhost:8080").build();
    }
  
    public Mono<String> getMessage() {
      return this.client.get().uri("/hello").accept(MediaType.APPLICATION_JSON)
          .retrieve()
          .bodyToMono(Greeting.class)
          .map(Greeting::getMessage);
    }
    
    public Mono<String> getSecondMessage() {
        return this.client.get().uri("/second-message")  // Assuming there's an endpoint like this
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(Greeting.class)
            .map(Greeting::getSecondMessage);  // This assumes Greeting class has a getSecondMessage() method that works similarly to getMessage()
    }

}
