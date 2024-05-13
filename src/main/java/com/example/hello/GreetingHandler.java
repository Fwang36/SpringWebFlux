package com.example.hello;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import io.sentry.Sentry;
import io.sentry.spring.jakarta.tracing.SentrySpan;
import reactor.core.publisher.Mono;

@Component
public class GreetingHandler {
    private final ExternalApiClient externalApiClient;
    
    public GreetingHandler(ExternalApiClient externalApiClient) {
        this.externalApiClient = externalApiClient;
    }
    @SentrySpan
    public Mono<ServerResponse> fetchExternalData(ServerRequest request) {
        return externalApiClient.fetchData()
            .flatMap(data -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(data))
            .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Error fetching external data: " + e.getMessage()));
    }
    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
          .body(BodyInserters.fromValue(new Greeting("Hello, Spring!")));
      }
    @SentrySpan(description = "Second Message Span")
    public Mono<ServerResponse> secondMessage(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
          .body(BodyInserters.fromValue(new Greeting("This is second message")));
      }  

    public Mono<Greeting> hello2() {
        return Mono.just(new Greeting("Hello, Spring!"));  
    }

//   @SentrySpan(description = "Second Message Span")
    public Mono<Greeting> secondMessage2() {
        return Mono.just(new Greeting("This is the second message"));
    }

    
    public Mono<ServerResponse> combinedMessages(ServerRequest request) {
        Mono<Greeting> helloMono = hello2();
        Mono<Greeting> secondMono = secondMessage2();

        
        return Mono.zip(helloMono, secondMono, (hello, second) -> 
            new Greeting("Combined messages: " + hello.getMessage() + " and " + second.getMessage()))
            .flatMap(combinedGreeting -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(combinedGreeting)));
    }

    public Mono<ServerResponse> throwError(ServerRequest request) {
        return Mono.error(new RuntimeException("Deliberate Exception Thrown"));
    }

    public Mono<ServerResponse> triggerError(ServerRequest request) {
        try {
            throw new Exception("This is a test.");
        } catch (Exception e) {
            Sentry.captureException(e);
            return ServerResponse
                .status(500)
                .bodyValue("Error has been captured by Sentry: " + e.getMessage());
        }
    }


}


