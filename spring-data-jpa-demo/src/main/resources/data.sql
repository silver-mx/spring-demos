INSERT INTO Accounts (account_id, account_name, email)
VALUES (1, 'moe', 'moe@example.com'),
       (2, 'larry', 'larry@example.com'),
       (3, 'curly', 'curley@example.com'),
       (4, 'shemp', 'shemp@example.com');

INSERT INTO Products (product_id, product_name)
VALUES (1, 'Open RoundFile'),
       (2, 'Visual TurboBuilder'),
       (3, 'ReConsider');

INSERT INTO BugStatus (status)
VALUES ('NEW'),
       ('IN PROGRESS'),
       ('CODE COMPLETE'),
       ('VERIFIED'),
       ('FIXED'),
       ('DUPLICATE'),
       ('WONTFIX');

INSERT INTO Bugs (bug_id, summary, reported_by)
VALUES (1234, 'crash when I save', 4),
       (2345, 'increase performance', 3),
       (3456, 'screen goes blank', 4),
       (5678, 'unknown conflict between products', 2);

INSERT INTO BugsProducts (bug_id, product_id)
VALUES (1234, 1),
       (1234, 3),
       (3456, 2),
       (5678, 1),
       (5678, 3);

INSERT INTO Comments (comment_id, bug_id, author, comment)
VALUES (6789, 1234, 4, 'It crashes!'),
       (9876, 2345, 1, 'Great idea!');

INSERT INTO Tags (bug_id, tag)
VALUES (1234, 'crash'),
       (1234, 'save'),
       (1234, 'version-3.0'),
       (1234, 'windows');

INSERT INTO Screenshots (bug_id, image_id, screenshot_image, caption)
VALUES (1234, 1, 'data', 'Image caption example');