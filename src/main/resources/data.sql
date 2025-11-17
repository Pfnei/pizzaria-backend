CREATE EXTENSION IF NOT EXISTS "pgcrypto";


INSERT INTO users (
    user_id, username, password, salutation, salutation_detail,
    firstname, lastname, email, phone_number, zipcode, city, country,
    address, created_at, last_updated_at,
    active,admin
) VALUES
-- Reduzierte Testdaten (5 User)
(gen_random_uuid(), 'clarkzod', '$2a$10$plRXfUw0I.tZH1mZ2HLV7Or4wJt9ZVKSfamZ/jP0f0Kr0vL3u.r.6', 'MS', NULL, 'Hannah', 'Bukovec', 'Hannah.Bukovec@muster.at', '+43 676 1234567', '4020', 'Linz', 'Austria', 'Hauptstraße 1', now(), now(), true, false),
(gen_random_uuid(), 'brucefox', '$2a$10$plRXfUw0I.tZH1mZ2HLV7Or4wJt9ZVKSfamZ/jP0f0Kr0vL3u.r.6', 'MR', NULL, 'Paul', 'Pfischer', 'Paul.Pfischer@muster.at', '+43 664 2345678', '8010', 'Graz', 'Austria', 'Eggenberger Allee 15', now(), now(), false, true ),
(gen_random_uuid(), 'diana91', '$2a$10$plRXfUw0I.tZH1mZ2HLV7Or4wJt9ZVKSfamZ/jP0f0Kr0vL3u.r.6', 'MS', NULL, 'Sophie', 'Schmid', 'Sophie.Schmid@muster.at', '+43 650 3456789', '5020', 'Salzburg', 'Austria', 'Getreidegasse 8', now(), now(), true, false),
(gen_random_uuid(), 'loganx', '$2a$10$plRXfUw0I.tZH1mZ2HLV7Or4wJt9ZVKSfamZ/jP0f0Kr0vL3u.r.6', 'MX', 'was weiß ich', 'Monday', 'Maier', 'Tobias.Maier@muster.at', '+43 699 4567890', '2700', 'Wiener Neustadt', 'Austria', 'Neunkirchner Straße 20', now(), now(), true, false),
(gen_random_uuid(), 'peterb', '$2a$10$plRXfUw0I.tZH1mZ2HLV7Or4wJt9ZVKSfamZ/jP0f0Kr0vL3u.r.6', 'MS', NULL, 'Katharina', 'Baumhackl', 'Katharina.Baumhackl@muster.at', '+43 676 9012345', '3100', 'St. Pölten', 'Austria', 'Kremser Gasse 7', now(), now(), true, true);


UPDATE users
SET
    created_by_user_id = user_id,
    last_updated_by_user_id = user_id;



INSERT INTO allergens (abbreviation, description, created_at, last_updated_at)
VALUES
    ('A', 'Glutenhaltiges Getreide', now(), now()),
    ('B', 'Krebstiere', now(), now()),
    ('C', 'Eier', now(), now()),
    ('D', 'Fisch', now(), now()),
    ('E', 'Erdnüsse', now(), now()),
    ('F', 'Soja', now(), now()),
    ('G', 'Milch (inkl. Laktose)', now(), now()),
    ('H', 'Schalenfrüchte (Nüsse)', now(), now()),
    ('L', 'Sellerie', now(), now()),
    ('M', 'Senf', now(), now()),
    ('N', 'Sesam', now(), now()),
    ('O', 'Schwefeldioxid und Sulfite', now(), now()),
    ('P', 'Lupinen', now(), now()),
    ('R', 'Weichtiere', now(), now());


UPDATE allergens
SET
    created_by_user_id = sub.user_id,
    last_updated_by_user_id = sub.user_id
    FROM (
         SELECT user_id
         FROM users
         WHERE admin = true
         ORDER BY random()
         LIMIT 1
     ) AS sub;

-- *****************
-- 3. PRODUCTS TABLE
-- *****************

INSERT INTO products (
    product_id, product_name, product_description, main_category,
    sub_category, price, vegetarian, created_at, last_updated_at,
    active, created_by_user_id, last_updated_by_user_id
) VALUES
-- STARTERS (3 Produkte)
(gen_random_uuid(), 'Bruschetta Classica', 'Geröstetes Brot mit Tomaten, Knoblauch & Basilikum', 'STARTER', NULL, 5.90, true, '2024-03-01', '2025-01-12', true, NULL, NULL),
(gen_random_uuid(), 'Caprese mit Mozzarella di Bufala', 'Buffalomozzarella mit Tomaten & Basilikum', 'STARTER', NULL, 7.20, true, '2024-04-12', '2025-03-18', true, NULL, NULL),
(gen_random_uuid(), 'Carpaccio vom Rind', 'Dünnes Rindfleisch, Parmesan & Rucola', 'STARTER', NULL, 11.50, false, '2024-08-23', '2025-01-04', true, NULL, NULL),

-- MAIN_COURSE PIZZA (5 Produkte)
(gen_random_uuid(), 'Pizza Margherita', 'Tomaten, Mozzarella & Basilikum', 'MAIN_COURSE', 'PIZZA', 8.90, true, '2025-01-22', '2025-05-10', true, NULL, NULL),
(gen_random_uuid(), 'Pizza Salami', 'Mit würziger Salami & Käse', 'MAIN_COURSE', 'PIZZA', 10.50, false, '2025-02-14', '2025-04-29', true, NULL, NULL),
(gen_random_uuid(), 'Pizza Funghi', 'Frische Champignons & Mozzarella', 'MAIN_COURSE', 'PIZZA', 9.90, true, '2025-03-02', '2025-04-27', true, NULL, NULL),
(gen_random_uuid(), 'Pizza Quattro Formaggi', 'Vier Käsesorten', 'MAIN_COURSE', 'PIZZA', 10.90, true, '2025-03-24', '2025-05-30', true, NULL, NULL),
(gen_random_uuid(), 'Pizza Diavola', 'Scharfe Salami & Chili', 'MAIN_COURSE', 'PIZZA', 11.50, false, '2025-04-01', '2025-06-02', true, NULL, NULL),

-- MAIN_COURSE PASTA (3 Produkte)
(gen_random_uuid(), 'Spaghetti Aglio e Olio', 'Knoblauch, Olivenöl & Chili', 'MAIN_COURSE', 'PASTA', 8.50, true, '2025-03-28', '2025-06-02', true, NULL, NULL),
(gen_random_uuid(), 'Tagliatelle al Ragù', 'Bandnudeln mit Fleischragù', 'MAIN_COURSE', 'PASTA', 12.80, false, '2025-04-04', '2025-06-08', true, NULL, NULL),
(gen_random_uuid(), 'Lasagne al Forno', 'Schichtweise mit Hack & Bechamel', 'MAIN_COURSE', 'PASTA', 13.20, false, '2025-04-20', '2025-06-15', true, NULL, NULL),

-- MAIN_COURSE BOWLS (2 Produkte)
(gen_random_uuid(), 'Veggie Buddha Bowl', 'Reis, Kichererbsen, Gemüse & Tahini', 'MAIN_COURSE', 'BOWL', 11.90, true, '2025-03-10', '2025-05-08', true, NULL, NULL),
(gen_random_uuid(), 'Chicken Teriyaki Bowl', 'Reis, Huhn, Sesam & Gemüse', 'MAIN_COURSE', 'BOWL', 13.90, false, '2025-03-27', '2025-05-20', true, NULL, NULL),

-- DESSERT (3 Produkte)
(gen_random_uuid(), 'Tiramisu', 'Mit Espresso & Mascarpone', 'DESSERT', NULL, 6.20, true, '2025-03-20', '2025-06-12', true, NULL, NULL),
(gen_random_uuid(), 'Panna Cotta', 'Mit Erdbeersauce', 'DESSERT', NULL, 5.80, true, '2025-03-25', '2025-06-15', true, NULL, NULL),
(gen_random_uuid(), 'Gelato Misto', 'Gemischtes Eis', 'DESSERT', NULL, 4.50, true, '2025-04-02', '2025-06-21', true, NULL, NULL),

-- DRINKS (5 Produkte)
(gen_random_uuid(), 'Coca-Cola', NULL, 'DRINK', 'ALCOHOL_FREE', 2.90, true, '2025-04-01', '2025-06-20', true, NULL, NULL),
(gen_random_uuid(), 'Mineralwasser', NULL, 'DRINK', 'ALCOHOL_FREE', 2.40, true, '2025-04-06', '2025-06-23', true, NULL, NULL),
(gen_random_uuid(), 'Hauswein Rot', NULL, 'DRINK', 'WINE', 3.90, true, '2025-04-10', '2025-06-24', true, NULL, NULL),
(gen_random_uuid(), 'Espresso', NULL, 'DRINK', 'COFFEE', 1.90, true, '2025-04-18', '2025-06-27', true, NULL, NULL),
(gen_random_uuid(), 'Ottakringer Helles', NULL, 'DRINK', 'BEER', 4.80, true, '2025-04-26', '2025-07-01', true, NULL, NULL);

-- Zuweisung des Erstellungs-/Änderungs-Users für Produkte (ein zufälliger Admin)
UPDATE products
SET
    created_by_user_id = sub.user_id,
    last_updated_by_user_id = sub.user_id
    FROM (
         SELECT user_id
         FROM users
         WHERE admin = true -- Korrigiert zu 'isAdmin'
         ORDER BY random()
         LIMIT 1
     ) AS sub;

-- PRODUCTS_ALLERGENS: Zuweisung von 1 bis 4 zufälligen Allergenen pro Produkt
INSERT INTO products_allergens (product_id, allergen_abbreviation)
SELECT
    p.product_id,
    a.abbreviation
FROM
    products p
        JOIN LATERAL (
        SELECT abbreviation
        FROM allergens
        ORDER BY random()
            LIMIT floor(random() * 4 + 1)::int
        ) AS a ON TRUE;

-- *****************
-- 4. ORDERS TABLE
-- *****************

INSERT INTO orders
(order_id, address, city, zipcode, firstname, lastname, phone_number,
 total, delivery_note, delivered_at, created_at, created_by)
VALUES
    -- Reduzierte Testdaten (5 Orders)
    (gen_random_uuid(), 'Hauptstraße 12', 'Linz', '4020', 'Hannah', 'Bukovec', '+43 676 1234567', 1, 'Bitte bei Nachbarn klingeln', '2025-06-20', '2025-06-19', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Mozartgasse 3', 'Graz', '8010', 'Paul', 'Pfischer', '+43 664 2345678', 1, 'Nicht vor 17 Uhr', '2025-06-18', '2025-06-17', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Salzachweg 22', 'Salzburg', '5020', 'Sophie', 'Schmid', '+43 650 3456789', 1, 'Torcode: 1234', '2025-06-15', '2025-06-14', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Bahnhofstraße 7', 'Wiener Neustadt', '2700', 'Monday', 'Maier', '+43 699 4567890', 1, 'Hund ist freundlich', '2025-06-12', '2025-06-11', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Poststraße 8', 'St. Pölten', '3100', 'Katharina', 'Baumhackl', '+43 676 9012345', 1, 'Türe ist offen', '2025-05-30', '2025-05-29', (SELECT user_id FROM users ORDER BY random() LIMIT 1));


-- *****************
-- 5. ORDER_ITEM & TOTAL CALCULATION
-- *****************

-- 5.1. Füllt order_item mit 1 bis 5 zufälligen Produkten pro Bestellung
WITH random_orders AS (
    SELECT
        o.order_id,
        p.product_id,
        p.product_name,
        p.price,
        ROW_NUMBER() OVER (PARTITION BY o.order_id ORDER BY random()) AS rn
    FROM orders o
             CROSS JOIN products p
)
INSERT INTO order_item (order_item_id, order_id, product_id, quantity, product_name, price)
SELECT
    gen_random_uuid(),
    ro.order_id,
    ro.product_id,
    1 AS quantity,
    ro.product_name,
    ro.price AS price
FROM random_orders ro
WHERE ro.rn <= FLOOR(random() * 5 + 1); -- 1 bis 5 Items pro Order

-- 5.2. Aktualisiert die Mengen (1-4) und berechnet den Posten-Preis
WITH updated_items AS (
    SELECT
        oi.order_item_id,
        FLOOR(random() * 4 + 1)::int AS quantity, -- Menge zwischen 1 und 4
        p.price AS single_price
    FROM order_item oi
             JOIN products p ON oi.product_id = p.product_id
)
UPDATE order_item
SET
    quantity = updated_items.quantity,
    price = updated_items.quantity * updated_items.single_price
    FROM updated_items
WHERE order_item.order_item_id = updated_items.order_item_id;

-- 5.3. Aktualisiert den Gesamtpreis (total) der Bestellungen
UPDATE orders
SET total = totals.sum_price
    FROM (
         SELECT
             order_id,
             SUM(price)::numeric(10, 2) AS sum_price
         FROM order_item
         GROUP BY order_id
     ) AS totals
WHERE orders.order_id = totals.order_id;