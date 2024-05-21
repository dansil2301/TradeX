INSERT INTO traders (username, email, password, status, registered_at)
VALUES ('john_doe', 'john@example.com', '$2a$10$2b0i9r5QF2F8qLJ/v3mTO.W92wD0ZrFqDyvuhkf4ie1eC3mkh1oQG', 'TRADER_BASIC', NOW());

INSERT INTO traders (username, email, password, status, registered_at)
VALUES ('jane_doe', 'jane@example.com', '$2a$10$2b0i9r5QF2F8qLJ/v3mTO.W92wD0ZrFqDyvuhkf4ie1eC3mkh1oQG', 'TRADER_PLUS', NOW());

INSERT INTO pages (page_name) VALUES ('Account');
INSERT INTO pages (page_name) VALUES ('Admin');
INSERT INTO pages (page_name) VALUES ('Terminal');

INSERT INTO pages_visited (user_id, page_id, visited_at) VALUES (1, 1, NOW());
INSERT INTO pages_visited (user_id, page_id, visited_at) VALUES (1, 3, NOW());

INSERT INTO pages_visited (user_id, page_id, visited_at) VALUES (2, 1, NOW());
INSERT INTO pages_visited (user_id, page_id, visited_at) VALUES (2, 1, NOW());