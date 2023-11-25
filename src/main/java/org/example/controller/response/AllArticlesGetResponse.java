package org.example.controller.response;

import org.example.entity.Article;

import java.util.List;

public record AllArticlesGetResponse(List<Article> articles) {
}
