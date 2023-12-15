package org.example.controller.request;

import org.example.entity.Comment;

import java.util.List;
import java.util.Set;

public record ArticleCreateRequest(String title, String content, Set<String> tags) {
}
