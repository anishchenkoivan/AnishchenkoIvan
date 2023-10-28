package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationTest {
    UserRepository userRepository;

    void fillUserRepository() {
        userRepository = new ConcurrentUserRepository();
        List<User> customUsers = List.of(new User("Vasya", "Ivanov", "1"), new User("John", "Wick", "123"));
        for (User user : customUsers) {
            userRepository.updateUserByMsisdn(user.msisdn, user);
        }
    }

    @Test
    void shouldReturnEnrichedMessage() {
        fillUserRepository();
        EnrichmentService enrichmentService = new EnrichmentService(new Enricher[]{new MsisdnEnricher()}, userRepository);
        Message message = new Message(Map.of("msisdn", "1"), EnrichmentType.MSISDN);

        Message enrichedMessage = enrichmentService.enrich(message);

        assertEquals(enrichedMessage, new Message(Map.of("msisdn", "1", "firstName", "Vasya", "lastName", "Ivanov"), EnrichmentType.MSISDN));
    }

    @Test
    void shouldSucceedEnrichmentConcurrentEnvironmentSuccessfully() throws InterruptedException {
        fillUserRepository();
        EnrichmentService enrichmentService = new EnrichmentService(new Enricher[]{new MsisdnEnricher()}, userRepository);
        List<Message> enrichmentResults = new CopyOnWriteArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);
        Message message = new Message(Map.of("msisdn", "123"), EnrichmentType.MSISDN);

        for (int i = 0; i < 5; i++) {
            executorService.submit(() -> {
                enrichmentResults.add(enrichmentService.enrich(message));
                latch.countDown();
            });
        }
        latch.await();

        for (Message enrichedMessage : enrichmentResults) {
            assertEquals(enrichedMessage, new Message(Map.of("msisdn", "123", "firstName", "John", "lastName", "Wick"), EnrichmentType.MSISDN));
        }
    }
}
