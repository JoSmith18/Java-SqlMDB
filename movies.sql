DROP TABLE movies CASCADE;
DROP TABLE ratings;

CREATE table movies (id Serial Unique Primary Key,student_name text, title text, main_char_name text);

Create table ratings (id Serial Unique Primary Key, movie_id Serial references movies (id), movie_rating decimal);

INSERT INTO movies (student_name, title, main_char_name) VALUES ('Jo','21 Jump Street', 'Channing Tatum');

INSERT INTO ratings (movie_id, movie_rating) VALUES (1, 5);