CREATE TABLE comments
(
    comment_id BIGSERIAL PRIMARY KEY,
    article_id BIGINT REFERENCES articles(article_id),
    text TEXT
)