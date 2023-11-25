package org.example.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.entity.id.ArticleId;
import org.example.entity.id.CommentId;

import java.util.Objects;

public class Comment {
    public final CommentId id;
    public final ArticleId articleId;
    public final String text;

    @JsonCreator
    public Comment(@JsonProperty("id") CommentId id, @JsonProperty("articleId") ArticleId articleId, @JsonProperty("text") String text) {
        this.id = id;
        this.articleId = articleId;
        this.text = text;
    }

    public CommentId getId() { return id; }
    public ArticleId getArticleId() { return articleId; }
    public String getText() { return text; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
