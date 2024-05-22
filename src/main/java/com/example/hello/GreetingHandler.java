package com.example.hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(GreetingHandler.class);
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
    @SentrySpan
    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
          .body(BodyInserters.fromValue(new Greeting("Hello, Spring!")));
      }
    @SentrySpan(description = "Second Message Span")
    public Mono<ServerResponse> secondMessage(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
          .body(BodyInserters.fromValue(new Greeting("This is second message")));
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
    //events created from these messages will be grouped together in 1 issue due to existence of stacktrace 
    public Mono<ServerResponse> logExampleMessages(ServerRequest request) {
        logger.trace("This is a TRACE level message.");
        logger.debug("This is a DEBUG level message.");
        logger.info("This is an INFO level message.");
        logger.warn("This is a WARN level message.");
        logger.error("This is an ERROR level message.");
        
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue("{\"message\": \"Logged example messages at all levels to console.\"}"));
        }

}


