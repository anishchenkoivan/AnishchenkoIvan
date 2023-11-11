package org.example.service;

import org.example.entity.id.ArticleId;
import org.example.repository.InMemoryArticleRepository;
import org.example.service.exceptions.CreateException;
import org.example.service.exceptions.DeleteException;
import org.example.service.exceptions.FindException;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {

    @Test
    void ShouldCreateArticle() {
        ArticleService articleService = new ArticleService(new InMemoryArticleRepository());
        assertDoesNotThrow(() -> articleService.create("Title", "Content text", Set.of("tagA")));
    }

    @Test
    void shouldFindArticleById() {
        ArticleService articleService = new ArticleService(new InMemoryArticleRepository());
        articleService.create("Title", "Content text", Set.of("tagA"));
        assertDoesNotThrow(() -> articleService.findById(new ArticleId(1)));
        assertThrows(FindException.class, () -> articleService.findById(new ArticleId(2)));
    }

    @Test
    void shouldDeleteArticleById() {
        ArticleService articleService = new ArticleService(new InMemoryArticleRepository());
        articleService.create("Title", "Content text", Set.of("tagA"));
        assertDoesNotThrow(() -> articleService.delete(new ArticleId(1)));
        assertThrows(DeleteException.class, () -> articleService.delete(new ArticleId(2)));
    }
}
