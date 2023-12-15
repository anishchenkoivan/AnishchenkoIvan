package org.example.repository;

import org.example.entity.Comment;
import org.example.entity.id.CommentId;
import org.example.repository.exceptions.ItemNotFoundException;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

public class PostgresCommentRepository implements CommentRepository {

    private final Jdbi jdbi;

    public PostgresCommentRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public CommentId generateId() {
        try (Handle handle = jdbi.open()) {
            long value = (long) handle.createQuery("SELECT nextval('comments_comment_id_seq') AS value")
                    .mapToMap()
                    .first()
                    .get("value");
            return new CommentId(value);
        }
    }

    @Override
    public void create(Comment comment) {
        jdbi.useTransaction((Handle handle) -> {
            int selectedRaws = handle.createUpdate(
                    "SELECT * FROM articles WHERE article_id = :articleId FOR UPDATE")
                    .bind("articleId", comment.getArticleId().getValue())
                    .execute();

            if (selectedRaws == 0) {
                throw new ItemNotFoundException("Could not find article with id=" + comment.getArticleId());
            }

            handle.createUpdate(
                    "INSERT INTO comments (comment_id, article_id, text) VALUES (:commentId, :articleId, :text)")
                    .bind("commentId", comment.getId().getValue())
                    .bind("articleId", comment.getArticleId().getValue())
                    .bind("text", comment.getText())
                    .execute();

            long amountOfComments = handle.createQuery(
                    "SELECT count(*) FROM comments WHERE article_id = :articleId")
                    .bind("articleId", comment.getArticleId().getValue())
                    .mapTo(long.class)
                    .one();

            if (amountOfComments > 3) {
                handle.createUpdate(
                        "UPDATE articles SET trending = true WHERE article_id = :articleId")
                        .bind("articleId", comment.getArticleId().getValue())
                        .execute();
            }
        });
    }

    @Override
    public void delete(CommentId commentId) {
        jdbi.useTransaction((Handle handle) -> {
            int selectedRaws = handle.createUpdate(
                            "SELECT * FROM articles WHERE article_id = :articleId FOR UPDATE")
                    .bind("articleId", commentId.getValue())
                    .execute();

            if (selectedRaws == 0) {
                throw new ItemNotFoundException("Could not find comment with id=" + commentId.getValue());
            }

            long articleId = handle.createQuery("SELECT article_id FROM comments WHERE comment_id = :commentId")
                    .bind("commentId", commentId.getValue())
                    .mapTo(long.class)
                    .one();

            handle.createUpdate(
                            "DELETE FROM comments WHERE comment_id = :commentId")
                    .bind("commentId", commentId.getValue())
                    .execute();

            long amountOfComments = handle.createQuery(
                            "SELECT count(*) FROM comments WHERE article_id = :articleId")
                    .bind("articleId", articleId)
                    .mapTo(long.class)
                    .one();

            if (amountOfComments <= 3) {
                handle.createUpdate(
                                "UPDATE articles SET trending = false WHERE article_id = :articleId")
                        .bind("articleId", articleId)
                        .execute();
            }
        });
    }
}
