package com.example.hello;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import io.sentry.spring.jakarta.tracing.SentrySpan;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration(proxyBeanMethods = false)
public class GreetingRouter {
    @Bean
    @SentrySpan
    public RouterFunction<ServerResponse> route(GreetingHandler greetingHandler) {
  
      return RouterFunctions
        //transaction
        .route(GET("/hello").and(accept(MediaType.APPLICATION_JSON)), greetingHandler::hello)  
        //another transaction
        .andRoute(GET("/second-message").and(accept(MediaType.APPLICATION_JSON)), greetingHandler::secondMessage)
        //crash
        .andRoute(GET("/error").and(accept(MediaType.APPLICATION_JSON)), greetingHandler::throwError)
        //transaction that makes an outgoing request
        .andRoute(GET("/external-data"), greetingHandler::fetchExternalData)
        //captureException
        .andRoute(GET("/trigger-error"), greetingHandler::triggerError);
    }
}

