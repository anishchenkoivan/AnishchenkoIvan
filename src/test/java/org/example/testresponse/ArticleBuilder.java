package org.example.testresponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.entity.Article;

public class ArticleBuilder {
    ArticleReadData articleData;

    @JsonCreator
    public ArticleBuilder(@JsonProperty("article") ArticleReadData articleReadData) {
        this.articleData = articleReadData;
    }

    public Article build() {
        return new Article(articleData.id(), articleData.title(), articleData.content(), articleData.tags(), articleData.comments());
    }
}
