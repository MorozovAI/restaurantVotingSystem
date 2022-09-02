INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2);

INSERT INTO restaurant (name)
VALUES ('Столовая №1'),
       ('Kofeman'),
       ('Sushisun'),
       ('Steakballs'),
       ('Чебургер');

INSERT INTO menu (name, menu_date, restaurant_id)
VALUES (CONCAT('Меню на ', CURRENT_DATE), CURRENT_DATE, 1),
       (CONCAT('Меню на ', CURRENT_DATE), CURRENT_DATE, 2),
       (CONCAT('Меню на ', CURRENT_DATE), CURRENT_DATE, 3),
       (CONCAT('Меню на ', CURRENT_DATE), CURRENT_DATE, 4),
       (CONCAT('Меню на ', CURRENT_DATE-1), CURRENT_DATE - 1, 1);

INSERT INTO dish (name, price, restaurant_id)
VALUES ('Чай', 99.99, 1),
       ('Кофе', 99.99, 1),
       ('Макароны с котлетой', 199.99, 1),
       ('Картофельное пюре с сосиской', 199.99, 1),
       ('Борщ', 299.99, 1),
       ('Щи', 299.99, 1),
       ('Капучино', 199.99, 2),
       ('Эспрессо', 199.99, 2),
       ('Яблочный штрудель', 199.99, 2),
       ('Маффин шоколадный', 199.99, 2),
       ('Вафля венская', 299.99, 2),
       ('Шоколадка', 299.99, 2),
       ('Чай', 149.99, 3),
       ('Пиво', 299.99, 3),
       ('Ролл Калифорния', 399.99, 3),
       ('Ролл Филадельфия', 199.99, 3),
       ('Унагисяки', 299.99, 3),
       ('Угорь в кляре', 399.99, 3),
       ('Чай', 129.99, 4),
       ('Кофе', 299.99, 4),
       ('Потанцуем', 399.99, 4),
       ('Ёжики со сметаной', 199.99, 4),
       ('Ленивые голубцы', 299.99, 4),
       ('Перец фаршированный', 399.99, 4),
       ('Лимонад Буратин', 199.99, 5),
       ('Чай черный', 129.99, 5),
       ('Чебурек', 399.99, 5),
       ('Чебурашка', 199.99, 5),
       ('Беляш с мясом', 299.99, 5),
       ('Беляш с картошкой', 399.99, 5);

INSERT INTO vote (user_id, vote_date, restaurant_id)
VALUES (1, NOW(), 2),
       (2, NOW(), 4),
       (3, NOW(), 1);

INSERT INTO dish_menu (menu_id, dish_id)
VALUES (1, 1),
       (1, 3),
       (1, 5),
       (2, 7),
       (2, 9),
       (2, 11),
       (3, 13),
       (3, 15),
       (3, 17),
       (4, 19),
       (4, 21),
       (4, 23)