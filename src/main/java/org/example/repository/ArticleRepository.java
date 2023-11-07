package org.example.repository;

import org.example.entity.Article;
import org.example.entity.id.ArticleId;

import java.util.List;

public interface ArticleRepository {
    ArticleId generateId();

    Article findById(ArticleId id);

    void create(Article article);

    void delete(ArticleId id);

    void update(Article article);

    List<Article> getAll();
}
