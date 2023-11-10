package org.example.controller;

import org.example.entity.Article;
import org.example.service.ArticleService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Service;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleFreemarkerController implements Controller {
    private final Service service;
    private final ArticleService articleService;
    private final FreeMarkerEngine freeMarkerEngine;

    public ArticleFreemarkerController(Service service, ArticleService articleService, FreeMarkerEngine freeMarkerEngine) {
        this.service = service;
        this.articleService = articleService;
        this.freeMarkerEngine = freeMarkerEngine;
    }

    @Override
    public void initializeEndpoints() {
        service.get("/", (Request request, Response response) -> {
            response.type("text/html; charset=utf-8");
            List<Article> articles = articleService.getAll();
            List<Map<String, String>> articleMapList = articles.stream().map(article -> Map.of("title", article.getTitle(), "comments", String.valueOf(article.getComments().size()))).toList();

            Map<String, Object> model = new HashMap<>();
            model.put("articles", articleMapList);
            return freeMarkerEngine.render(new ModelAndView(model, "index.ftl"));
        });
    }
}
