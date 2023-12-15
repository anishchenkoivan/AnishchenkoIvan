package org.example.service;

import org.example.entity.Article;
import org.example.entity.Comment;
import org.example.entity.id.ArticleId;
import org.example.entity.id.CommentId;
import org.example.repository.CommentRepository;
import org.example.repository.exceptions.ItemNotFoundException;
import org.example.service.exceptions.CreateException;
import org.example.service.exceptions.DeleteException;
import org.jdbi.v3.core.transaction.TransactionException;

public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentId addComment(ArticleId articleId, String text) {
        CommentId commentId = commentRepository.generateId();
        Comment comment = new Comment(commentId, articleId, text);
        try {
            commentRepository.create(comment);
        } catch (TransactionException e) {
            throw new CreateException("Couldn't create comment", e);
        }
        return commentId;
    }

    public void deleteComment(CommentId commentId) {
        try {
            commentRepository.delete(commentId);
        } catch (ItemNotFoundException e) {
            throw new DeleteException("Couldn't delete delete comment with id=" + commentId);
        }
    }
}
