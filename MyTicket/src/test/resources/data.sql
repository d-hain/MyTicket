-- Event
INSERT INTO event (id, name, from_date, to_date, description) VALUES (1, 'Luffys Birthday', '1999-05-05', '1999-05-05', 'MEAAAAAAAAAT');
INSERT INTO event (id, name, from_date, to_date, description) VALUES (2, 'Rogers Birthday', '1999-12-31', '1999-12-31', 'The first Pirate King');
-- Ticket Category
INSERT INTO category (id, name, price, stock, event_id) VALUES (1, 'VEGETABLES', 100, 10, 1);
INSERT INTO category (id, name, price, stock, event_id) VALUES (2, 'FISH', 200, 5, 1);
INSERT INTO category (id, name, price, stock, event_id) VALUES (3, 'MEAT', 100000, 1, 1);
INSERT INTO category (id, name, price, stock, event_id) VALUES (4, 'Joyboy', 10000, 1, 2);
INSERT INTO category (id, name, price, stock, event_id) VALUES (5, 'Rayleigh', 1000, 1, 2);
INSERT INTO category (id, name, price, stock, event_id) VALUES (6, 'Ace', 1000, 2, 2);
-- Ticket
INSERT INTO ticket (id, category_id) VALUES (1, 1);
INSERT INTO ticket (id, category_id) VALUES (2, 1);
INSERT INTO ticket (id, category_id) VALUES (3, 1);
INSERT INTO ticket (id, category_id) VALUES (4, 1);
INSERT INTO ticket (id, category_id) VALUES (5, 1);
INSERT INTO ticket (id, category_id) VALUES (6, 1);
INSERT INTO ticket (id, category_id) VALUES (7, 1);
INSERT INTO ticket (id, category_id) VALUES (8, 1);
INSERT INTO ticket (id, category_id) VALUES (9, 1);
INSERT INTO ticket (id, category_id) VALUES (10, 1);
INSERT INTO ticket (id, category_id) VALUES (11, 2);
INSERT INTO ticket (id, category_id) VALUES (12, 2);
INSERT INTO ticket (id, category_id) VALUES (13, 2);
INSERT INTO ticket (id, category_id) VALUES (14, 2);
INSERT INTO ticket (id, category_id) VALUES (15, 2);
INSERT INTO ticket (id, category_id) VALUES (16, 3);
INSERT INTO ticket (id, category_id) VALUES (17, 4);
INSERT INTO ticket (id, category_id) VALUES (18, 5);
INSERT INTO ticket (id, category_id) VALUES (19, 6);
INSERT INTO ticket (id, category_id) VALUES (20, 6);
-- Cart
INSERT INTO cart (id) VALUES (1);
-- Cart Tickets
INSERT INTO cart_tickets (carts_id, tickets_id) VALUES (1, 16);
INSERT INTO cart_tickets (carts_id, tickets_id) VALUES (1, 17);
-- User
-- Password: DefinitelyIsNotTheRulerOfTheWorld777666
INSERT INTO user (id, email, first_name, last_name, password, role) VALUES (1, 'imu@world.gov', 'Imu', 'イム', '$2a$10$9uFYYUqceM8g9yylPeqMIejkabZHXhdq3fIIhqCERjLXM9Tulmw.S', 0);
-- Password: MEAT
INSERT INTO user (id, email, first_name, last_name, password, role, cart_id) VALUES (2, 'monkey.d.luffy@straw.hat', 'Luffy', 'Monkey D.', '$2a$10$77gQN2ciH3EfPdQ2uzb4EOWZ.34UTcimecxXqZPFhC/qejPqrvyFK', 1, 1);