INSERT INTO USER (role, user_id, username, password, email, avatar, registration_date, description, display_name) VALUES ('ADMIN', 1, 'milan', '$2a$04$vdDSumH7RTKuFjnVEfUHF.rVddWtujOx2F5npibBBGcXnXWHHaBVa', 'milan@gmail.com', 'https://www.redditinc.com/assets/images/galleries/snoo-small.png', '2022-5-28', 'Milan Description', 'milanM001');
INSERT INTO USER (role, user_id, username, password, email, avatar, registration_date, description, display_name) VALUES ('USER', 2, 'luka', '$2a$04$HzqVuw/D4LcKaAXDx1UXeOQ5gzt33F3ONd7D3BYiwnLcfa4fRSMOS', 'luka@gmail.com', 'https://www.redditinc.com/assets/images/galleries/snoo-small.png', '2022-5-28', 'Luka Description', 'LukaL001');
SELECT * FROM USER;
DELETE FROM USER WHERE USER_ID = ?;



INSERT INTO POST (post_id, title, text, creation_date, image_path, user_id, community_id, flair_id) VALUES (1, 'Todays Weather', 'Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit...', '2020-4-10', 'https://res.cloudinary.com/dtpgi0zck/image/upload/s--eWjIe4k---/c_fill,h_260,w_380/v1/EducationHub/photos/lightning-bolts.jpg', 1, 1, 1);
INSERT INTO POST (post_id, title, text, creation_date, image_path, user_id, community_id, flair_id) VALUES (2, 'New Car', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer id odio eu mauris dignissim aliquam. Phasellus in ante ac ipsum pharetra commodo.', '2020-4-10', 'https://www.evoximages.com/wp-content/uploads/2021/09/img_0_0_30-e1630602054753.jpg', 1, 1, 2);
INSERT INTO POST (post_id, title, text, creation_date, image_path, user_id, community_id, flair_id) VALUES (3, 'Forecast Tomorrow', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer id odio eu mauris dignissim aliquam. Phasellus in ante ac ipsum pharetra commodo.', '2020-4-10', 'https://media.istockphoto.com/vectors/weather-icons-set-vector-id1225639749?k=20&m=1225639749&s=612x612&w=0&h=wMzoyNFDyx6ewmaBU_lnqp1R7EEpyvxVkdBcgb1Yt0o=', 2, 2, 2);
INSERT INTO POST (post_id, title, text, creation_date, image_path, user_id, community_id, flair_id) VALUES (4, 'Bomb.', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer id odio eu mauris dignissim aliquam. Phasellus in ante ac ipsum pharetra commodo.', '2020-4-10', 'https://i.insider.com/593af2ddbf76bb47028b4c5d?width=700', 2, 2, 2);
SELECT * FROM POST;
DELETE FROM POST WHERE POST_ID = ?;



INSERT INTO COMMENT (comment_id, is_deleted, text, timestamp, post_id, user_id) VALUES (1, false, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer id odio eu mauris dignissim aliquam. Phasellus in ante ac ipsum pharetra commodo.', '2020-4-10', 1, 1);
INSERT INTO COMMENT (comment_id, is_deleted, text, timestamp, post_id, user_id) VALUES (2, false, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer id odio eu mauris dignissim aliquam. Phasellus in ante ac ipsum pharetra commodo.', '2020-4-10', 1, 1);
SELECT * FROM COMMENT;
DELETE FROM COMMENT WHERE COMMENT_ID = ?;



INSERT INTO COMMUNITY (community_id, name, description, creation_date, is_suspended, suspended_reason) VALUES (1, 'r/World', 'Some random text to fill the empty space.', '2022-5-28', false, 'Not Suspended');
INSERT INTO COMMUNITY (community_id, name, description, creation_date, is_suspended, suspended_reason) VALUES (2, 'r/Country', 'Some random text to fill the empty space.', '2022-5-28', false, 'Not Suspended');
SELECT * FROM COMMUNITY;
DELETE FROM COMMUNITY WHERE COMMUNITY_ID = ?;



INSERT INTO FLAIR (flair_id, name) VALUES (1, "Fun");
INSERT INTO FLAIR (flair_id, name) VALUES (2, "Sport");
INSERT INTO FLAIR (flair_id, name) VALUES (3, "Activity");
INSERT INTO FLAIR (flair_id, name) VALUES (4, "News");
SELECT * FROM FLAIR;
DELETE FROM FLAIR WHERE FLAIR_ID = ?;



INSERT INTO COMMUNITY_FLAIRS(flair_id, community_id) VALUES (1, 1);
INSERT INTO COMMUNITY_FLAIRS(flair_id, community_id) VALUES (2, 1);
INSERT INTO COMMUNITY_FLAIRS(flair_id, community_id) VALUES (3, 2);
INSERT INTO COMMUNITY_FLAIRS(flair_id, community_id) VALUES (4, 2);
SELECT * FROM COMMUNITY_FLAIRS;
DELETE FROM COMMUNITY_FLAIRS WHERE FLAIR_ID = ?;



INSERT INTO RULE (rule_id, description, community_id) VALUES (1, "No Harassment", 1);
INSERT INTO RULE (rule_id, description, community_id) VALUES (2, "No Racism", 1);
INSERT INTO RULE (rule_id, description, community_id) VALUES (3, "No Hate Speech", 2);
INSERT INTO RULE (rule_id, description, community_id) VALUES (4, "No Swearing", 2);
SELECT * FROM RULE;
DELETE FROM RULE WHERE RULE_ID = ?;



INSERT INTO MODERATOR (moderator_id, community_id, user_id) VALUES (1, 1, 1);
INSERT INTO MODERATOR (moderator_id, community_id, user_id) VALUES (2, 2, 1);
SELECT * FROM MODERATOR;
DELETE FROM MODERATOR WHERE MODERATOR_ID = ?;