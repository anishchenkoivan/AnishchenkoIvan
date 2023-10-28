package org.example;

import java.util.Map;

public record Message(Map<String, String> content, EnrichmentType enrichmentType) { }
