package org.example.repository;

import org.example.entity.Comment;
import org.example.entity.id.CommentId;

public interface CommentRepository {
    CommentId generateId();

    void create(Comment comment);

    void delete(CommentId commentId);
}
