package org.example;

public interface Enricher {
    Message enrich(Message message, UserRepository userRepository);
    EnrichmentType getEnrichmentType();
}
