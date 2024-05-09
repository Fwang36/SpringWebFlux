package com.example.hello;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class GreetingHandler {
    private final ExternalApiClient externalApiClient;
    public GreetingHandler(ExternalApiClient externalApiClient) {
        this.externalApiClient = externalApiClient;
    }

        public Mono<ServerResponse> fetchExternalData(ServerRequest request) {
        return externalApiClient.fetchData()
            .flatMap(data -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(data))
            .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Error fetching external data: " + e.getMessage()));
    }
    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
          .body(BodyInserters.fromValue(new Greeting("Hello, Spring!")));
      }
      
      public Mono<ServerResponse> secondMessage(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
          .body(BodyInserters.fromValue(new Greeting("This is second message")));
      }  

    public Mono<ServerResponse> combinedMessages(ServerRequest request) {
        Mono<String> hello = Mono.just("Hello, Spring!");
        Mono<String> second = Mono.just("This is the second message");
    
        return Mono.zip(hello, second, (h, s) -> new Greeting(h + " and " + s))
            .flatMap(greeting -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(greeting)));
    }

    public Mono<ServerResponse> throwError(ServerRequest request) {
        return Mono.error(new RuntimeException("Deliberate Exception Thrown"));
    }
}


