package org.example.repository;

import org.example.entity.Article;
import org.example.entity.id.ArticleId;
import org.example.repository.exceptions.ItemNotFoundException;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

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
                        (Arrays.stream((String[]) result.get("tags")).collect(Collectors.toSet())),
                        ((boolean) result.get("trending"))
                );
                    });
        } catch (NullPointerException e) {
            throw new ItemNotFoundException("Could not find article with id=" + id);
        }
    }

    @Override
    public void create(Article article) {
        jdbi.useTransaction((Handle handle) -> handle.createUpdate(
                "INSERT INTO articles (article_id, title, content, tags, trending) VALUES (:articleId, :title, :content, :tags, :trending)")
                .bind("articleId", article.getId())
                .bind("title", article.getTitle())
                .bind("content", article.getContent())
                .bind("tags", article.getTags().toArray())
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
                    .bind("article_id", article.getId().getValue())
                    .bind("title", article.getTitle())
                    .bind("content", article.getContent())
                    .bind("tags", article.getTags().toArray())
                    .bind("trending", article.getTrending())
                    .execute();
        });
    }

    @Override
    public List<Article> getAll() {
        return null;
    }
}
