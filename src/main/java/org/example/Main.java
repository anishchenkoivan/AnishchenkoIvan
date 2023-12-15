package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.example.controller.ArticleController;
import org.example.controller.ArticleFreemarkerController;
import org.example.controller.CommentController;
import org.example.repository.ArticleRepository;
import org.example.repository.CommentRepository;
import org.example.repository.PostgresArticleRepository;
import org.example.repository.PostgresCommentRepository;
import org.example.service.ArticleService;
import org.example.service.CommentService;
import org.example.template.TemplateFactory;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import spark.Service;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Config config = ConfigFactory.load();

        Flyway flyway = Flyway.configure().outOfOrder(true).locations("classpath:db/migrations").dataSource(config.getString("app.database.url"), config.getString("app.database.user"), config.getString("app.database.password")).load();
        flyway.migrate();

        Jdbi jdbi = Jdbi.create(config.getString("app.database.url"), config.getString("app.database.user"), config.getString("app.database.password"));

        Service service = Service.ignite();
        ObjectMapper objectMapper = new ObjectMapper();

        ArticleRepository articleRepository = new PostgresArticleRepository(jdbi);
        CommentRepository commentRepository = new PostgresCommentRepository(jdbi);
        ArticleService articleService = new ArticleService(articleRepository);
        CommentService commentService = new CommentService(commentRepository);
        Application application = new Application(List.of(
                new ArticleController(service, articleService, objectMapper),
                new CommentController(service, commentService, objectMapper),
                new ArticleFreemarkerController(service, articleService, TemplateFactory.freeMarkerEngine())));
        application.start();
    }
}
