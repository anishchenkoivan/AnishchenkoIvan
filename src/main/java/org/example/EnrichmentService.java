package org.example;

public class EnrichmentService {
    private final Enricher[] enrichers;
    private final UserRepository userRepository;


    public EnrichmentService(Enricher[] enrichers, UserRepository userRepository) {
        this.enrichers = enrichers;
        this.userRepository = userRepository;
    }

    public Message enrich(Message message) {
        for (Enricher enricher: enrichers) {
            if (enricher.getEnrichmentType().equals(message.enrichmentType())) {
                return enricher.enrich(message, userRepository);
            }
        }
        return message;
    }
}
