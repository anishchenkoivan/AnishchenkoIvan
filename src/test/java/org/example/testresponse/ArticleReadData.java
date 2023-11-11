package org.example.testresponse;

import org.example.entity.Comment;
import org.example.entity.id.ArticleId;

import java.util.List;
import java.util.Set;

public record ArticleReadData(ArticleId id, String title, String content, Set<String> tags, List<Comment> comments) {
}
