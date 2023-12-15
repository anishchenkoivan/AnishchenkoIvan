package org.example.repository;

import org.example.entity.Article;
import org.example.entity.id.ArticleId;
import org.example.repository.exceptions.ItemNotFoundException;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.postgresql.jdbc.PgArray;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PostgresArticleRepository implements ArticleRepository{

    private final Jdbi jdbi;

    public PostgresArticleRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public ArticleId generateId() {
        try (Handle handle = jdbi.open()) {
            long value = (long) handle.createQuery(
                    "SELECT nextval('articles_article_id_seq') AS value")
                    .mapToMap()
                    .first()
                    .get("value");
            return new ArticleId(value);
        }
    }

    @Override
    public Article findById(ArticleId id) {
        try {
            return jdbi.inTransaction((Handle handle) -> {
                Map<String, Object> result =
                        handle.createQuery(
                                "SELECT article_id, title, content, tags, trending FROM articles WHERE article_id = :articleId")
                                .bind("articleId", id.getValue())
                                .mapToMap()
                                .first();
                return new Article(
                        (new ArticleId((long) result.get("article_id"))),
                        ((String) result.get("title")),
                        ((String) result.get("content")),
                        (Arrays.stream((String[]) ((PgArray) result.get("tags")).getArray()).collect(Collectors.toSet())),
                        ((boolean) result.get("trending"))
                );
                    });
        } catch (NullPointerException | SQLException e) {
            throw new ItemNotFoundException("Could not find article with id=" + id);
        }
    }

    @Override
    public void create(Article article) {
        jdbi.useTransaction((Handle handle) -> handle.createUpdate(
                "INSERT INTO articles (article_id, title, content, tags, trending) VALUES (:articleId, :title, :content, :tags, :trending)")
                .bind("articleId", article.getId().getValue())
                .bind("title", article.getTitle())
                .bind("content", article.getContent())
                .bind("tags", article.getTags().toArray(new String[0]))
                .bind("trending", article.getTrending())
                .execute());
    }

    @Override
    public void delete(ArticleId id) {
        jdbi.useTransaction((Handle handle) -> {
            long updatedRaws = handle.createUpdate(
                    "SELECT * FROM articles WHERE article_id = :articleId FOR UPDATE")
                    .bind("articleId", id.getValue())
                    .execute();

            if (updatedRaws == 0) {
                throw new ItemNotFoundException("Could not find article with id=" + id);
            }

            handle.createUpdate(
                    "DELETE FROM comments WHERE article_id = :articleId")
                    .bind("articleId", id.getValue())
                    .execute();

            handle.createUpdate(
                    "DELETE FROM articles WHERE article_id = :articleId")
                    .bind("articleId", id.getValue())
                    .execute();
        });
    }

    @Override
    public void update(Article article) {
        jdbi.useTransaction((Handle handle) -> {
            long updatedRaws = handle.createUpdate(
                    "SELECT * FROM articles WHERE article_id = :articleId FOR UPDATE")
                    .bind("articleId", article.getId().getValue())
                    .execute();

            if (updatedRaws == 0) {
                throw new ItemNotFoundException("Could not find article with id=" + article.getId());
            }

            handle.createUpdate(
                "UPDATE articles SET title = :title, content = :content, tags = :tags, trending = :trending WHERE article_id = :articleId")
                    .bind("articleId", article.getId().getValue())
                    .bind("title", article.getTitle())
                    .bind("content", article.getContent())
                    .bind("tags", article.getTags().toArray(new String[0]))
                    .bind("trending", article.getTrending())
                    .execute();
        });
    }

    @Override
    public List<Article> getAll() {
        try {
            return jdbi.inTransaction((Handle handle) -> handle.createQuery(
                    "SELECT article_id, title, content, tags, trending FROM articles")
                    .mapToMap()
                    .list()
                    .stream()
                    .map((Map<String, Object> result) -> {
                        try {
                            return new Article(
                                    new ArticleId((long) result.get("article_id")),
                                    (String) result.get("title"),
                                    (String) result.get("content"),
                                    (Arrays.stream((String[]) ((PgArray) result.get("tags")).getArray()).collect(Collectors.toSet())),
                                    (boolean) result.get("trending"));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }))
                    .collect(Collectors.toList());
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
    }
}
