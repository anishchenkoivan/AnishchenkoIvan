--Task 1
SELECT count(profile.profile_id)
FROM profile
         LEFT JOIN post ON profile.profile_id = post.profile_id
WHERE post.post_id IS null;


--Task 2
SELECT post.post_id
FROM post
         JOIN comment ON post.post_id = comment.post_id
WHERE post.title ~ '^[0-9]' AND length(post.content) > 20
GROUP BY post.post_id, post.title
HAVING count (comment_id) = 2
ORDER BY post_id;


--Task 3
SELECT post.post_id
FROM post
         LEFT JOIN comment ON post.post_id = comment.post_id
GROUP BY post.post_id
HAVING count(comment.comment_id) < 2
ORDER BY post.post_id LIMIT 10;