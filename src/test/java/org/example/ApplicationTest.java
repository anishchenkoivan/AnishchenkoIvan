package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.ArticleController;
import org.example.controller.ArticleFreemarkerController;
import org.example.controller.CommentController;
import org.example.entity.Article;
import org.example.repository.*;
import org.example.service.ArticleService;
import org.example.service.CommentService;
import org.example.template.TemplateFactory;
import org.example.testresponse.ArticleBuilder;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import spark.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class ApplicationTest {

    @Container
    public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:13");

    private static Jdbi jdbi;

    private Service service;

    @BeforeAll
    static void beforeAll() {
        String postgresJdbcUrl = POSTGRES.getJdbcUrl();
        Flyway flyway = Flyway.configure()
                .outOfOrder(true)
                .locations("classpath:db/migrations")
                .dataSource(postgresJdbcUrl, POSTGRES.getUsername(), POSTGRES.getPassword())
                .load();
        flyway.migrate();
        jdbi = Jdbi.create(postgresJdbcUrl, POSTGRES.getUsername(), POSTGRES.getPassword());
    }

    @BeforeEach
    void beforeEach() {
        service = Service.ignite();
    }

    @AfterEach
    void afterEach() {
        service.stop();
        service.awaitStop();
    }

    @Test
    void endToEndApplicationTest() throws IOException, InterruptedException {
        ArticleRepository articleRepository = new PostgresArticleRepository(jdbi);
        ArticleService articleService = new ArticleService(articleRepository);
        CommentRepository commentRepository = new PostgresCommentRepository(jdbi);
        CommentService commentService = new CommentService(commentRepository);
        ObjectMapper objectMapper = new ObjectMapper();
        Application application = new Application(List.of(
                new ArticleController(service, articleService, objectMapper),
                new CommentController(service, commentService, objectMapper)));
        application.start();
        service.awaitInitialization();
        HttpResponse<String> response;

        //Create article
        response = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder()
                        .POST(BodyPublishers.ofString(
                                """
                                        {"title":"Title","content":"content text","tags":["tagA","tabB"]}"""
                        ))
                        .uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
                );

        assertEquals(201, response.statusCode());

        //Add comment
        response = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder()
                        .method("POST", BodyPublishers.ofString(
                                """
                                        {"text":"comment text"}"""
                        ))
                        .uri(URI.create("http://localhost:%d/api/comments/add-comment/1".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
                );

        assertEquals(201, response.statusCode());
        System.out.println(response.body());

        //Edit article content
        response = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder()
                        .method("PATCH", BodyPublishers.ofString(
                                """
                                        {"content":"updated article content"}"""
                        ))
                        .uri(URI.create("http://localhost:%d/api/articles/update-content/1".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
                );

        assertEquals(201, response.statusCode());

        //Delete comment
        response = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder()
                        .method("DELETE", BodyPublishers.ofString(
                                """
                                        {"commentId":{"value":1}}"""
                        ))
                        .uri(URI.create("http://localhost:%d/api/comments/delete-comment".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
                );

        assertEquals(204, response.statusCode());

        //Get All articles
        response = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder()
                        .GET()
                        .uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
                );

        assertEquals(201, response.statusCode());

        //Get and verify article by id
        response = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder()
                        .GET()
                        .uri(URI.create("http://localhost:%d/api/articles/1".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
                );

        assertEquals(201, response.statusCode());
        System.out.println(response.body());
        Article article = objectMapper.readValue(response.body(), ArticleBuilder.class).build();
        assertEquals("updated article content", article.getContent());

        //Delete article
        response = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder()
                        .DELETE()
                        .uri(URI.create("http://localhost:%d/api/articles/1".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
                );

        assertEquals(204, response.statusCode());
    }

    @Test
    void endToEndTemplateTest() throws IOException, InterruptedException {
        ArticleRepository articleRepository = new PostgresArticleRepository(jdbi);
        ArticleService articleService = new ArticleService(articleRepository);
        CommentRepository commentRepository = new PostgresCommentRepository(jdbi);
        CommentService commentService = new CommentService(commentRepository);
        ObjectMapper objectMapper = new ObjectMapper();
        Application application = new Application(List.of(
                new ArticleController(service, articleService, objectMapper),
                new CommentController(service, commentService, objectMapper),
                new ArticleFreemarkerController(service, articleService, TemplateFactory.freeMarkerEngine())));
        application.start();
        service.awaitInitialization();

        //Create article
        HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder()
                        .POST(BodyPublishers.ofString(
                                """
                                        {"title":"Title","content":"content text","tags":["tagA","tabB"]}"""
                        ))
                        .uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
                );

        //Add comment
        HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder()
                        .method("PATCH", BodyPublishers.ofString(
                                """
                                        {"text":"comment text"}"""
                        ))
                        .uri(URI.create("http://localhost:%d/api/comments/add-comment".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
                );

        //Get html
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder()
                        .GET()
                        .uri(URI.create("http://localhost:%d/".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
                );

        assertThat(response.body(), containsString("""
                    </tr>
                            <tr>
                            <td>Title</td>
                        </tr>\
                """));
    }
}
