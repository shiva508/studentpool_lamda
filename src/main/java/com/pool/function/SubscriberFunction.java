package com.pool.function;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.cloud.function.context.MessageRoutingCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import com.pool.model.Subscriber;
import com.pool.service.SubscriberService;

import reactor.core.publisher.Flux;

@Configuration
public class SubscriberFunction {
    private final SubscriberService subscriberService;

    public SubscriberFunction(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @Bean
    public Supplier<List<Subscriber>> findAll() {
        return () -> subscriberService.getAll();
    }

    @Bean
    public Consumer<String> create() {
        return (email) -> subscriberService.addSubscriber(email);
    }

    @Bean
    public Function<Flux<String>, Flux<String>> uppercaseFn() {
        return name -> name.map(String::toUpperCase);
    }

    @Bean
    public Function<Flux<String>, Flux<Integer>> lengthFn() {
        return name -> name.map(String::length);
    }

    @Bean
    public Supplier<Flux<String>> shiva() {
        return () -> {
            String v1 = String.valueOf(System.nanoTime());
            String v2 = String.valueOf(System.nanoTime());
            String v3 = String.valueOf(System.nanoTime());
            return Flux.just(v1, v2, v3);
        };
    }

    @Bean
    public MessageRoutingCallback functionReouting() {
        return new MessageRoutingCallback() {
            @Override
            public FunctionRoutingResult routingResult(Message<?> message) {
                return new FunctionRoutingResult((String) message.getHeaders().get("fun-name"));
            }
        };
    }
}
