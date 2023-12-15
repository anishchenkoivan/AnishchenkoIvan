CREATE TABLE articles
(
    article_id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200),
    content TEXT,
    tags VARCHAR(50)[],
    trending BOOLEAN
)
