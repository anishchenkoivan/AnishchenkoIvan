package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.ArticleController;
import org.example.controller.ArticleFreemarkerController;
import org.example.repository.InMemoryArticleRepository;
import org.example.service.ArticleService;
import org.example.template.TemplateFactory;
import spark.Service;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Service service = Service.ignite();
        ObjectMapper objectMapper = new ObjectMapper();
        final var articleService = new ArticleService(new InMemoryArticleRepository());
        Application application = new Application(List.of(new ArticleController(service, articleService, objectMapper), new ArticleFreemarkerController(service, articleService, TemplateFactory.freeMarkerEngine())));
        application.start();
    }
}