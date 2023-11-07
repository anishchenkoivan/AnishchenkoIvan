package org.example.service;

import org.example.entity.Article;
import org.example.entity.Comment;
import org.example.entity.id.ArticleId;
import org.example.entity.id.CommentId;
import org.example.repository.ArticleRepository;
import org.example.repository.exceptions.DuplicateIdException;
import org.example.repository.exceptions.ItemNotFoundException;
import org.example.service.exceptions.CreateException;
import org.example.service.exceptions.DeleteException;
import org.example.service.exceptions.FindException;
import org.example.service.exceptions.UpdateException;

import java.util.List;
import java.util.Set;

public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public ArticleId create(String title, String content, Set<String> tags, List<Comment> comments) {
        ArticleId articleId = articleRepository.generateId();
        Article article = new Article(articleId, title, content, tags, comments);
        try {
            articleRepository.create(article);
        } catch (DuplicateIdException e) {
            throw new CreateException("Couldn't delete article", e);
        }
        return articleId;
    }

    public List<Article> getAll() {
        return articleRepository.getAll();
    }

    public Article findById(ArticleId id) {
        try {
            return articleRepository.findById(id);
        } catch (ItemNotFoundException e) {
            throw new FindException("Couldn't find article with id=" + id, e);
        }
    }

    public synchronized CommentId addComment(ArticleId articleId, String commentText) {
        Article article;
        try {
            article = articleRepository.findById(articleId);
        } catch (ItemNotFoundException e) {
            throw new UpdateException("Couldn't find article with id=" + articleId, e);
        }

        Article updatedArticle = article.addComment(commentText);
        try {
            articleRepository.update(updatedArticle);
            return updatedArticle.getComments().get(updatedArticle.getComments().size() - 1).getId();
        } catch (ItemNotFoundException e) {
            throw new UpdateException("couldn't find article with id=" + articleId, e);
        }
    }

    public synchronized void updateContent(ArticleId articleId, String content) {
        Article article;
        try {
            article = articleRepository.findById(articleId);
        } catch (ItemNotFoundException e) {
            throw new UpdateException("Couldn't find article with id=" + articleId, e);
        }

        try {
            articleRepository.update(article.withContent(content));
        } catch (ItemNotFoundException e) {
            throw new UpdateException("Couldn't find article with id=" + articleId, e);
        }
    }

    public void delete(ArticleId articleId) {
        try {
            articleRepository.delete(articleId);
        } catch (ItemNotFoundException e) {
            throw new DeleteException("Couldn't delete delete article with id=" + articleId, e);
        }
    }

    public synchronized void deleteComment(ArticleId articleId, CommentId commentId) {
        Article article;
        try {
            article = articleRepository.findById(articleId);
        } catch (ItemNotFoundException e) {
            throw new DeleteException("Couldn't find article with id=" + articleId, e);
        }

        Article updatedArticle = article.deleteComment(commentId);
        if (updatedArticle.getComments().size() == article.getComments().size()) {
            throw new DeleteException("Couldn't find comment with id=" + commentId);
        }

        try {
            articleRepository.update(updatedArticle);
        } catch (ItemNotFoundException e) {
            throw new DeleteException("Couldn't find article with id=" + articleId, e);
        }
    }
}
