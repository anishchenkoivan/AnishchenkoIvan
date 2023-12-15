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
    private final boolean trending;

    public Article(ArticleId id, String title, String content, Set<String> tags, boolean trending) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.trending = trending;
    }

    public ArticleId getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public Set<String> getTags() { return tags; }
    public boolean getTrending() { return trending; }

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

    public Article withContent(String content) {
        return new Article(this.id, this.title, content, this.tags, this.trending);
    }
}
