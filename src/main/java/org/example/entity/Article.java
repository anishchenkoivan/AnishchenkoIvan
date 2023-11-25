package org.example.entity;

import org.example.entity.id.ArticleId;
import org.example.entity.id.CommentId;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class Article {
    private final ArticleId id;
    private final String title;
    private final String content;
    private final Set<String> tags;
    private final List<Comment> comments;
    private final AtomicLong nextCommentId = new AtomicLong(0);

    public Article(ArticleId id, String title, String content, Set<String> tags, List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.comments = comments;
    }

    public ArticleId getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public Set<String> getTags() { return tags; }
    public List<Comment> getComments() {return comments; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Article addComment(String commentText) {
        Comment comment = new Comment(new CommentId(nextCommentId.incrementAndGet()), this.id, commentText);
        List<Comment> updatedComments = Stream.concat(comments.stream(), Stream.of(comment)).toList();
        return new Article(this.id, this.title, this.content, this.tags, updatedComments);
    }

    public Article deleteComment(CommentId commentId) {
        List<Comment> updatedComments = comments.stream().filter(streamComment -> !streamComment.getId().equals(commentId)).toList();
        return new Article(this.id, this.title, this.content, this.tags, updatedComments);
    }

    public Article withContent(String content) {
        return new Article(this.id, this.title, content, this.tags, this.comments);
    }
}
