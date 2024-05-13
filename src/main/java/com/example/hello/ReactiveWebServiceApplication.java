package com.example.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Configuration
@EnableScheduling
public class ReactiveWebServiceApplication {

    public static void main(String[] args) {
      ConfigurableApplicationContext context = SpringApplication.run(ReactiveWebServiceApplication.class, args);
      GreetingClient greetingClient = context.getBean(GreetingClient.class);
      // We need to block for the content here or the JVM might exit before the message is logged
      System.out.println(">> message = " + greetingClient.getMessage().block());
      System.out.println(">> second message = " + greetingClient.getSecondMessage().block());
        // Corrected print statement
    }
  }
