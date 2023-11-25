package org.example.repository;

import org.example.entity.Article;
import org.example.entity.id.ArticleId;
import org.example.repository.exceptions.DuplicateIdException;
import org.example.repository.exceptions.ItemNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryArticleRepository implements ArticleRepository{
    private final AtomicLong nextId = new AtomicLong(0);
    private final Map<ArticleId, Article> articlesMap = new ConcurrentHashMap<>();

    @Override
    public ArticleId generateId() {
        return new ArticleId(nextId.incrementAndGet());
    }

    @Override
    public Article findById(ArticleId id) {
        Article article = articlesMap.get(id);
        if (article == null) {
            throw new ItemNotFoundException("Could not find article with id=" + id);
        }
        return article;
    }

    @Override
    public synchronized void create(Article article) {
        if (articlesMap.get(article.getId()) != null) {
            throw new DuplicateIdException(String.format("Article with id=%s already exists", article.getId()));
        }
        articlesMap.put(article.getId(), article);
    }

    @Override
    public synchronized void delete(ArticleId id) {
        if (articlesMap.remove(id) == null) {
            throw new ItemNotFoundException("Could not find article with id=" + id);
        }
    }

    @Override
    public synchronized void update(Article article) {
        if (articlesMap.get(article.getId()) == null) {
            throw new ItemNotFoundException("Could not find article with id=" + article.getId());
        }
        articlesMap.put(article.getId(), article);
    }

    @Override
    public List<Article> getAll() {
        return articlesMap.values().stream().toList();
    }
}
