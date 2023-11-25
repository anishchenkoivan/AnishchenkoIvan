package org.example.controller.request;

import org.example.entity.id.CommentId;

public record CommentDeleteRequest(CommentId commentId) {
}
