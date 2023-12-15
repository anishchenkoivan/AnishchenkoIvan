package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.request.CommentAddRequest;
import org.example.controller.request.CommentDeleteRequest;
import org.example.controller.response.CommentAddResponse;
import org.example.controller.response.CommentDeleteResponse;
import org.example.controller.response.ErrorResponse;
import org.example.entity.id.ArticleId;
import org.example.entity.id.CommentId;
import org.example.service.CommentService;
import org.example.service.exceptions.DeleteException;
import org.example.service.exceptions.UpdateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Service;

public class CommentController implements Controller {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleController.class);

    private final Service service;
    private final CommentService commentService;
    private final ObjectMapper objectMapper;

    public CommentController(Service service, CommentService commentService, ObjectMapper objectMapper) {
        this.service = service;
        this.commentService = commentService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void initializeEndpoints() {
        addComment();
        deleteComment();
    }

    public void addComment() {
        service.post("api/comments/add-comment/:articleId", (Request request, Response response) -> {
            response.type("application/json");
            String body = request.body();
            ArticleId articleId = new ArticleId(Long.parseLong(request.params("articleId")));
            CommentAddRequest addCommentRequest = objectMapper.readValue(body, CommentAddRequest.class);
            try {
                CommentId commentId = commentService.addComment(articleId, addCommentRequest.text());
                LOG.debug("Comment added");
                response.status(201);
                return objectMapper.writeValueAsString(new CommentAddResponse(commentId));
            } catch (UpdateException e) {
                LOG.warn("Couldn't add comment", e);
                response.status(400);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
            }
        });
    }

        public void deleteComment() {
        service.delete("/api/comments/delete-comment", (Request request, Response response) -> {
            response.type("application/json");
            String body = request.body();
            CommentDeleteRequest commentDeleteRequest = objectMapper.readValue(body, CommentDeleteRequest.class);
            try {
                commentService.deleteComment(commentDeleteRequest.commentId());
                LOG.debug("Comment deleted");
                response.status(204);
                return objectMapper.writeValueAsString(new CommentDeleteResponse());
            } catch (DeleteException e) {
                LOG.warn("Couldn't create comment", e);
                response.status(400);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
            }
        });
    }
}
