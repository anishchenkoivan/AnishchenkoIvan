package org.example;

import java.util.HashMap;
import java.util.Map;

public class MsisdnEnricher implements Enricher{
    @Override
    public Message enrich(Message message, UserRepository userRepository) {
        Map<String, String> enrichedContent = new HashMap<>(message.content());
        if (message.content().containsKey("msisdn")) {
            User requiredUser = userRepository.findByMsisdn(message.content().get("msisdn"));
            enrichedContent.put("firstName", requiredUser.firstName);
            enrichedContent.put("lastName", requiredUser.lastName);
        }
        return new Message(enrichedContent, message.enrichmentType());
    }

    @Override
    public EnrichmentType getEnrichmentType() {
        return EnrichmentType.MSISDN;
    }


}
