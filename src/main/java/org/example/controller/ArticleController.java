package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.request.CommentAddRequest;
import org.example.controller.request.ArticleCreateRequest;
import org.example.controller.request.CommentDeleteRequest;
import org.example.controller.request.ContentUpdateRequest;
import org.example.controller.response.*;
import org.example.entity.id.ArticleId;
import org.example.entity.id.CommentId;
import org.example.service.ArticleService;
import org.example.service.exceptions.CreateException;
import org.example.service.exceptions.DeleteException;
import org.example.service.exceptions.FindException;
import org.example.service.exceptions.UpdateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Service;

public class ArticleController implements Controller {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleController.class);

    private final Service service;
    private final ArticleService articleService;
    private final ObjectMapper objectMapper;

    public ArticleController(Service service, ArticleService articleService, ObjectMapper objectMapper) {
        this.service = service;
        this.articleService = articleService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void initializeEndpoints() {
        getAllArticles();
        getSingleArticle();
        createArticle();
        updateContent();
        deleteArticle();
    }

    public void getAllArticles() {
        service.get("api/articles", (Request request, Response response) -> {
            response.type("application/json");
            response.status(201);
            LOG.debug("All articles received");
            return objectMapper.writeValueAsString(new AllArticlesGetResponse(articleService.getAll()));
        });
    }

    public void getSingleArticle() {
        service.get("api/articles/:articleId", (Request request, Response response) -> {
            response.type("application/json");
            ArticleId articleId = new ArticleId(Long.parseLong(request.params("articleId")));
            try {
                LOG.debug("Article with id={} received", articleId);
                response.status(201);
                return objectMapper.writeValueAsString(new SingleArticleGetResponse(articleService.findById(articleId)));
            } catch (FindException e) {
                LOG.warn("Couldn't find article", e);
                response.status(400);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
            }
        });
    }
//
//    public void addComment() {
//        service.patch("api/articles/add-comment/:articleId", (Request request, Response response) -> {
//            response.type("application/json");
//            String body = request.body();
//            ArticleId articleId = new ArticleId(Long.parseLong(request.params("articleId")));
//            CommentAddRequest addCommentRequest = objectMapper.readValue(body, CommentAddRequest.class);
//            try {
//                CommentId commentId = articleService.addComment(articleId, addCommentRequest.text());
//                LOG.debug("Comment added");
//                response.status(201);
//                return objectMapper.writeValueAsString(new CommentAddResponse(commentId));
//            } catch (UpdateException e) {
//                LOG.warn("Couldn't add comment", e);
//                response.status(400);
//                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
//            }
//        });
//    }

    public void createArticle() {
        service.post("/api/articles", (Request request, Response response) -> {
            response.type("application/json");
            String body = request.body();
            ArticleCreateRequest articleCreateRequest = objectMapper.readValue(body, ArticleCreateRequest.class);
            try {
                ArticleId articleId = articleService.create(articleCreateRequest.title(), articleCreateRequest.content(), articleCreateRequest.tags());
                LOG.debug("Article with id={} created", articleId);
                response.status(201);
                return objectMapper.writeValueAsString(new ArticleCreateResponse(articleId));
            } catch (CreateException e) {
                LOG.warn("Couldn't create article", e);
                response.status(400);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
            }
        });
    }

    public void updateContent() {
        service.patch("/api/articles/update-content/:articleId", (Request request, Response response) -> {
            response.type("application/json");
            String body = request.body();
            ArticleId articleId = new ArticleId(Long.parseLong(request.params("articleId")));
            ContentUpdateRequest contentUpdateRequest = objectMapper.readValue(body, ContentUpdateRequest.class);
            try {
                articleService.updateContent(articleId, contentUpdateRequest.content());
                LOG.debug("Updated content of article with id={}", articleId);
                response.status(201);
                return objectMapper.writeValueAsString(new ContentUpdateResponse());
            } catch (UpdateException e) {
                LOG.warn("Failed to update article content", e);
                response.status(400);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
            }
        });
    }

    public void deleteArticle() {
        service.delete("/api/articles/:articleId", (Request request, Response response) -> {
            response.type("application/json");
            ArticleId articleId = new ArticleId(Long.parseLong(request.params("articleId")));
            try {
                articleService.delete(articleId);
                LOG.debug("Article deleted");
                response.status(204);
                return objectMapper.writeValueAsString(new ArticleDeleteResponse());
            } catch (DeleteException e) {
                LOG.warn("Couldn't delete article", e);
                response.status(400);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
            }
        });
    }

//    public void deleteComment() {
//        service.delete("/api/articles/delete-comment/:articleId", (Request request, Response response) -> {
//            response.type("application/json");
//            ArticleId articleId = new ArticleId(Long.parseLong(request.params("articleId")));
//            String body = request.body();
//            CommentDeleteRequest commentDeleteRequest = objectMapper.readValue(body, CommentDeleteRequest.class);
//            try {
//                articleService.deleteComment(articleId, commentDeleteRequest.commentId());
//                LOG.debug("Comment deleted");
//                response.status(204);
//                return objectMapper.writeValueAsString(new CommentDeleteResponse());
//            } catch (DeleteException e) {
//                LOG.warn("Couldn't create comment", e);
//                response.status(400);
//                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
//            }
//        });
//    }
}
