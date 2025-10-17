CREATE EXTENSION IF NOT EXISTS "pgcrypto";

INSERT INTO users (
    user_id, username, password, salutation, salutation_detail,
    firstname, surname, email, phone_number, zipcode, city, country,
    address, created_at, last_updated_at,
    is_active, is_admin
) VALUES
      (gen_random_uuid(), 'clarkzod', '$2a$10$plRXfUw0I.tZH1mZ2HLV7Or4wJt9ZVKSfamZ/jP0f0Kr0vL3u.r.6', 'Frau', NULL, 'Hannah', 'Bukovec', 'Hannah.Bukovec@muster.at', '+43 676 1234567', '4020', 'Linz', 'Austria', 'Hauptstraße 1', now(), now(), true, false),
      (gen_random_uuid(), 'brucefox', '$2a$10$plRXfUw0I.tZH1mZ2HLV7Or4wJt9ZVKSfamZ/jP0f0Kr0vL3u.r.6', 'Herr', NULL, 'Paul', 'Pfischer', 'Paul.Pfischer@muster.at', '+43 664 2345678', '8010', 'Graz', 'Austria', 'Eggenberger Allee 15', now(), now(), false, true),
      (gen_random_uuid(), 'diana91', '$2a$10$plRXfUw0I.tZH1mZ2HLV7Or4wJt9ZVKSfamZ/jP0f0Kr0vL3u.r.6', 'Frau', NULL, 'Sophie', 'Schmid', 'Sophie.Schmid@muster.at', '+43 650 3456789', '5020', 'Salzburg', 'Austria', 'Getreidegasse 8', now(), now(), true, false),
      (gen_random_uuid(), 'loganx', '$2a$10$plRXfUw0I.tZH1mZ2HLV7Or4wJt9ZVKSfamZ/jP0f0Kr0vL3u.r.6', 'Divers', 'was weiß ich', 'Monday', 'Maier', 'Tobias.Maier@muster.at', '+43 699 4567890', '2700', 'Wiener Neustadt', 'Austria', 'Neunkirchner Straße 20', now(), now(), true, false),
      (gen_random_uuid(), 'selkyle', '$2a$10$plRXfUw0I.tZH1mZ2HLV7Or4wJt9ZVKSfamZ/jP0f0Kr0vL3u.r.6', 'Frau', NULL, 'Anna', 'Alang', 'Anna.Alang@muster.at', '+43 676 5678901', '6850', 'Dornbirn', 'Austria', 'Marktplatz 3', now(), now(), true, false),
      (gen_random_uuid(), 'parjord', '$2a$10$plRXfUw0I.tZH1mZ2HLV7Or4wJt9ZVKSfamZ/jP0f0Kr0vL3u.r.6', 'Herr', NULL, 'Nico', 'Hofer', 'Nico.Hofer@muster.at', '+43 664 6789012', '4400', 'Steyr', 'Austria', 'Stadtplatz 12', now(), now(), true, false),
      (gen_random_uuid(), 'barryv', '$2a$10$plRXfUw0I.tZH1mZ2HLV7Or4wJt9ZVKSfamZ/jP0f0Kr0vL3u.r.6', 'Frau', NULL, 'Lisa', 'Gruber', 'Lisa.Gruber@muster.at', '+43 650 7890123', '9500', 'Villach', 'Austria', 'Italiener Straße 25', now(), now(), false, false),
      (gen_random_uuid(), 'reeddoc', '$2a$10$plRXfUw0I.tZH1mZ2HLV7Or4wJt9ZVKSfamZ/jP0f0Kr0vL3u.r.6', 'Herr', NULL, 'David', 'Zuccatto', 'David.Zuccatto@muster.at', '+43 699 8901234', '3430', 'Tulln', 'Austria', 'Bahnhofstraße 4', now(), now(), true, false),
      (gen_random_uuid(), 'peterb', '$2a$10$plRXfUw0I.tZH1mZ2HLV7Or4wJt9ZVKSfamZ/jP0f0Kr0vL3u.r.6', 'Frau', NULL, 'Katharina', 'Baumhackl', 'Katharina.Baumhackl@muster.at', '+43 676 9012345', '3100', 'St. Pölten', 'Austria', 'Kremser Gasse 7', now(), now(), true, true),
      (gen_random_uuid(), 'tonystar', '$2a$10$plRXfUw0I.tZH1mZ2HLV7Or4wJt9ZVKSfamZ/jP0f0Kr0vL3u.r.6', 'Divers', 'was weiß ich', 'Morning', 'Müller', 'Raphael.Müller@muster.at', '+43 664 0123456', '7000', 'Eisenstadt', 'Austria', 'Haydngasse 11', now(), now(), true, false),
      (gen_random_uuid(), 'steverog', '$2a$10$plRXfUw0I.tZH1mZ2HLV7Or4wJt9ZVKSfamZ/jP0f0Kr0vL3u.r.6', 'Frau', NULL, 'Theresa', 'Chang', 'Theresa.Chang@muster.at', '+43 650 1234567', '1190', 'Wien', 'Austria', 'Grinzinger Straße 14', now(), now(), true, false),
      (gen_random_uuid(), 'natroma', '$2a$10$plRXfUw0I.tZH1mZ2HLV7Or4wJt9ZVKSfamZ/jP0f0Kr0vL3u.r.6', 'Herr', NULL, 'Simon', 'Schuster', 'Simon.Schuster@muster.at', '+43 699 2345678', '9560', 'Feldkirchen', 'Austria', 'Hauptplatz 9', now(), now(), true, true),
      (gen_random_uuid(), 'bamwayne', '$2a$10$plRXfUw0I.tZH1mZ2HLV7Or4wJt9ZVKSfamZ/jP0f0Kr0vL3u.r.6', 'Frau', NULL, 'Nina', 'Wiener', 'Nina.Wiener@muster.at', '+43 676 3456789', '2500', 'Baden', 'Austria', 'Helenenstraße 6', now(), now(), true, false);

UPDATE users
SET
    created_by_user_id = user_id,
    last_updated_by_user_id = user_id;
INSERT INTO allergens (abbreviation, description,
                       created_at, last_updated_at)
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
         WHERE is_admin = true
         ORDER BY random()
         LIMIT 1
     ) AS sub;



INSERT INTO products (
    product_id, product_name, product_description, main_category,
    sub_category, price, is_vegetarian, created_at, last_updated_at,
    is_active, created_by_user_id, last_updated_by_user_id
) VALUES
-- STARTERS
(gen_random_uuid(), 'Bruschetta Classica', 'Geröstetes Brot mit Tomaten, Knoblauch & Basilikum', 'STARTER', NULL, 5.90, true, '2024-03-01', '2025-01-12', true, NULL, NULL),
(gen_random_uuid(), 'Caprese mit Mozzarella di Bufala', 'Buffalomozzarella mit Tomaten & Basilikum', 'STARTER', NULL, 7.20, true, '2024-04-12', '2025-03-18', true, NULL, NULL),
(gen_random_uuid(), 'Antipasto Misto', 'Oliven, Schinken & Käse Variation', 'STARTER', NULL, 9.80, false, '2024-06-07', '2024-11-22', false, NULL, NULL),
(gen_random_uuid(), 'Knoblauchbrot', 'Knoblauchbutter auf Baguette', 'STARTER', NULL, 3.50, true, '2024-07-19', '2024-10-03', false, NULL, NULL),
(gen_random_uuid(), 'Carpaccio vom Rind', 'Dünnes Rindfleisch, Parmesan & Rucola', 'STARTER', NULL, 11.50, false, '2024-08-23', '2025-01-04', true, NULL, NULL),
(gen_random_uuid(), 'Vitello Tonnato', 'Kalbfleisch mit Thunfischcreme', 'STARTER', NULL, 10.20, false, '2024-08-30', '2025-01-08', true, NULL, NULL),
(gen_random_uuid(), 'Gegrilltes Gemüse', 'Mediterran mariniert', 'STARTER', NULL, 6.50, true, '2024-09-12', '2025-01-10', true, NULL, NULL),
(gen_random_uuid(), 'Zucchini Fritti', 'Frittierte Zucchinischeiben', 'STARTER', NULL, 5.20, true, '2024-09-22', '2025-01-15', true, NULL, NULL),
(gen_random_uuid(), 'Mozzarella in Carrozza', 'Frittierter Mozzarella', 'STARTER', NULL, 6.80, true, '2024-10-01', '2025-01-18', true, NULL, NULL),
(gen_random_uuid(), 'Oliven & Brot', 'Gemischte Oliven mit Ciabatta', 'STARTER', NULL, 4.00, true, '2024-10-15', '2025-01-20', true, NULL, NULL),

-- MAIN_COURSE PIZZA
(gen_random_uuid(), 'Pizza Margherita', 'Tomaten, Mozzarella & Basilikum', 'MAIN_COURSE', 'PIZZA', 8.90, true, '2025-01-22', '2025-05-10', true, NULL, NULL),
(gen_random_uuid(), 'Pizza Salami', 'Mit würziger Salami & Käse', 'MAIN_COURSE', 'PIZZA', 10.50, false, '2025-02-14', '2025-04-29', true, NULL, NULL),
(gen_random_uuid(), 'Pizza Funghi', 'Frische Champignons & Mozzarella', 'MAIN_COURSE', 'PIZZA', 9.90, true, '2025-03-02', '2025-04-27', true, NULL, NULL),
(gen_random_uuid(), 'Pizza Tonno', 'Thunfisch & rote Zwiebeln', 'MAIN_COURSE', 'PIZZA', 11.20, false, '2025-03-18', '2025-05-15', true, NULL, NULL),
(gen_random_uuid(), 'Pizza Quattro Formaggi', 'Vier Käsesorten', 'MAIN_COURSE', 'PIZZA', 10.90, true, '2025-03-24', '2025-05-30', true, NULL, NULL),
(gen_random_uuid(), 'Pizza Diavola', 'Scharfe Salami & Chili', 'MAIN_COURSE', 'PIZZA', 11.50, false, '2025-04-01', '2025-06-02', true, NULL, NULL),
(gen_random_uuid(), 'Pizza Prosciutto', 'Kochschinken & Mozzarella', 'MAIN_COURSE', 'PIZZA', 10.80, false, '2025-04-06', '2025-06-05', true, NULL, NULL),
(gen_random_uuid(), 'Pizza Vegetariana', 'Paprika, Zucchini, Aubergine', 'MAIN_COURSE', 'PIZZA', 9.80, true, '2025-04-10', '2025-06-10', true, NULL, NULL),
(gen_random_uuid(), 'Pizza Calzone', 'Gefüllt mit Schinken & Käse', 'MAIN_COURSE', 'PIZZA', 11.30, false, '2025-04-14', '2025-06-12', true, NULL, NULL),
(gen_random_uuid(), 'Pizza Frutti di Mare', 'Meeresfrüchte & Knoblauchöl', 'MAIN_COURSE', 'PIZZA', 12.90, false, '2025-04-18', '2025-06-14', true, NULL, NULL),

-- MAIN_COURSE PASTA
(gen_random_uuid(), 'Spaghetti Aglio e Olio', 'Knoblauch, Olivenöl & Chili', 'MAIN_COURSE', 'PASTA', 8.50, true, '2025-03-28', '2025-06-02', true, NULL, NULL),
(gen_random_uuid(), 'Tagliatelle al Ragù', 'Bandnudeln mit Fleischragù', 'MAIN_COURSE', 'PASTA', 12.80, false, '2025-04-04', '2025-06-08', true, NULL, NULL),
(gen_random_uuid(), 'Penne Arrabiata', 'Scharfe Tomatensauce', 'MAIN_COURSE', 'PASTA', 9.10, true, '2025-04-12', '2025-06-10', true, NULL, NULL),
(gen_random_uuid(), 'Lasagne al Forno', 'Schichtweise mit Hack & Bechamel', 'MAIN_COURSE', 'PASTA', 13.20, false, '2025-04-20', '2025-06-15', true, NULL, NULL),
(gen_random_uuid(), 'Gnocchi Gorgonzola', 'In cremiger Käsesauce', 'MAIN_COURSE', 'PASTA', 10.50, true, '2025-04-24', '2025-06-17', true, NULL, NULL),
(gen_random_uuid(), 'Linguine Frutti di Mare', 'Mit Meeresfrüchten', 'MAIN_COURSE', 'PASTA', 14.30, false, '2025-04-28', '2025-06-20', true, NULL, NULL),
(gen_random_uuid(), 'Spaghetti Carbonara', 'Mit Speck & Ei', 'MAIN_COURSE', 'PASTA', 11.00, false, '2025-05-01', '2025-06-22', true, NULL, NULL),
(gen_random_uuid(), 'Penne Pesto', 'Basilikumpesto & Parmesan', 'MAIN_COURSE', 'PASTA', 9.70, true, '2025-05-05', '2025-06-24', true, NULL, NULL),

-- MAIN_COURSE BOWLS
(gen_random_uuid(), 'Veggie Buddha Bowl', 'Reis, Kichererbsen, Gemüse & Tahini', 'MAIN_COURSE', 'BOWL', 11.90, true, '2025-03-10', '2025-05-08', true, NULL, NULL),
(gen_random_uuid(), 'Steak Bowl', 'Rind, Avocado & BBQ-Salat', 'MAIN_COURSE', 'BOWL', 15.30, false, '2025-03-16', '2025-05-11', true, NULL, NULL),
(gen_random_uuid(), 'Falafel Bowl', 'Humus, Falafel, Couscous', 'MAIN_COURSE', 'BOWL', 10.80, true, '2025-03-22', '2025-05-15', true, NULL, NULL),
(gen_random_uuid(), 'Chicken Teriyaki Bowl', 'Reis, Huhn, Sesam & Gemüse', 'MAIN_COURSE', 'BOWL', 13.90, false, '2025-03-27', '2025-05-20', true, NULL, NULL),

-- DESSERT
(gen_random_uuid(), 'Tiramisu', 'Mit Espresso & Mascarpone', 'DESSERT', NULL, 6.20, true, '2025-03-20', '2025-06-12', true, NULL, NULL),
(gen_random_uuid(), 'Panna Cotta', 'Mit Erdbeersauce', 'DESSERT', NULL, 5.80, true, '2025-03-25', '2025-06-15', true, NULL, NULL),
(gen_random_uuid(), 'Profiteroles', 'Gefüllt mit Vanillecreme', 'DESSERT', NULL, 5.90, true, '2025-03-28', '2025-06-18', true, NULL, NULL),
(gen_random_uuid(), 'Gelato Misto', 'Gemischtes Eis', 'DESSERT', NULL, 4.50, true, '2025-04-02', '2025-06-21', true, NULL, NULL),
(gen_random_uuid(), 'Käsekuchen', 'Hausgemacht mit Himbeeren', 'DESSERT', NULL, 5.70, true, '2025-04-08', '2025-06-24', true, NULL, NULL),

-- DRINKS
(gen_random_uuid(), 'Coca-Cola', NULL, 'DRINK', 'ALCOHOL_FREE', 2.90, true, '2025-04-01', '2025-06-20', true, NULL, NULL),
(gen_random_uuid(), 'Fanta', NULL, 'DRINK', 'ALCOHOL_FREE', 2.90, true, '2025-04-03', '2025-06-21', true, NULL, NULL),
(gen_random_uuid(), 'Sprite', NULL, 'DRINK','ALCOHOL_FREE', 2.90, true, '2025-04-05', '2025-06-22', true, NULL, NULL),
(gen_random_uuid(), 'Mineralwasser', NULL, 'DRINK', 'ALCOHOL_FREE', 2.40, true, '2025-04-06', '2025-06-23', true, NULL, NULL),
(gen_random_uuid(), 'Hauswein Rot', NULL, 'DRINK', 'WINE', 3.90, true, '2025-04-10', '2025-06-24', true, NULL, NULL),
(gen_random_uuid(), 'Hauswein Weiß', NULL, 'DRINK', 'WINE', 3.90, true, '2025-04-12', '2025-06-25', true, NULL, NULL),
(gen_random_uuid(), 'Aperol Spritz', NULL, 'DRINK', 'WINE', 5.90, true, '2025-04-15', '2025-06-26', true, NULL, NULL),
(gen_random_uuid(), 'Espresso', NULL, 'DRINK', 'COFFEE', 1.90, true, '2025-04-18', '2025-06-27', true, NULL, NULL),
(gen_random_uuid(), 'Cappuccino', NULL, 'DRINK', 'COFFEE', 2.80, true, '2025-04-20', '2025-06-28', true, NULL, NULL),
(gen_random_uuid(), 'Latte Macchiato', NULL, 'DRINK', 'COFFEE', 3.10, true, '2025-04-22', '2025-06-29', true, NULL, NULL),
(gen_random_uuid(), 'Grappa', NULL, 'DRINK', 'SPIRIT', 3.50, true, '2025-04-24', '2025-06-30', true, NULL, NULL),
(gen_random_uuid(), 'Limoncello', NULL, 'DRINK', 'SPIRIT', 3.20, true, '2025-04-26', '2025-07-01', true, NULL, NULL),
(gen_random_uuid(), 'Campari Orange', NULL, 'DRINK', 'SPIRIT', 5.20, true, '2025-04-28', '2025-07-02', true, NULL, NULL),
(gen_random_uuid(), 'Ottakringer Helles', NULL, 'DRINK', 'BEER', 4.80, true, '2025-04-26', '2025-07-01', true, NULL, NULL),
(gen_random_uuid(), 'Ottakringer Zwickl', NULL, 'DRINK', 'BEER', 4.80, true, '2025-04-26', '2025-07-01', true, NULL, NULL),
(gen_random_uuid(), 'Ottakringer Pils', NULL, 'DRINK', 'BEER', 4.80, true, '2025-04-26', '2025-07-01', true, NULL, NULL);

UPDATE products
SET
    created_by_user_id = sub.user_id,
    last_updated_by_user_id = sub.user_id
FROM (
         SELECT user_id
         FROM users
         WHERE is_admin = true
         ORDER BY random()
         LIMIT 1
     ) AS sub;

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



INSERT INTO orders
(order_id, address, city, zipcode, firstname, surname, phone_number,
 total, delivery_note, delivered_at, created_at, created_by)
VALUES
    (gen_random_uuid(), 'Hauptstraße 12', 'Linz', '4020', 'Hannah', 'Bukovec', '+43 676 1234567', 1, 'Bitte bei Nachbarn klingeln', '2025-06-20', '2025-06-19', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Mozartgasse 3', 'Graz', '8010', 'Paul', 'Pfischer', '+43 664 2345678', 1, 'Nicht vor 17 Uhr', '2025-06-18', '2025-06-17', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Salzachweg 22', 'Salzburg', '5020', 'Sophie', 'Schmid', '+43 650 3456789', 1, 'Torcode: 1234', '2025-06-15', '2025-06-14', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Bahnhofstraße 7', 'Wiener Neustadt', '2700', 'Monday', 'Maier', '+43 699 4567890', 1, 'Hund ist freundlich', '2025-06-12', '2025-06-11', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Marktplatz 5', 'Dornbirn', '6850', 'Anna', 'Alang', '+43 676 5678901', 1, 'Bitte anrufen', '2025-06-10', '2025-06-09', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Schillerstraße 9', 'Steyr', '4400', 'Nico', 'Hofer', '+43 664 6789012', 1, 'Hintereingang nutzen', '2025-06-08', '2025-06-07', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Feldweg 4', 'Villach', '9500', 'Lisa', 'Gruber', '+43 650 7890123', 1, 'Paketbox vorhanden', '2025-06-05', '2025-06-04', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Ringstraße 16', 'Tulln', '3430', 'David', 'Zuccatto', '+43 699 8901234', 1, 'Klingel defekt', '2025-06-02', '2025-06-01', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Poststraße 8', 'St. Pölten', '3100', 'Katharina', 'Baumhackl', '+43 676 9012345', 1, 'Türe ist offen', '2025-05-30', '2025-05-29', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Donaustraße 11', 'Eisenstadt', '7000', 'Morning', 'Müller', '+43 664 0123456', 1, 'Beim Briefkasten ablegen', '2025-05-28', '2025-05-27', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Blumengasse 2', 'Wien', '1190', 'Theresa', 'Chang', '+43 650 1234567', 1, 'Bitte leise klingeln', '2025-05-25', '2025-05-24', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Karawankenweg 13', 'Feldkirchen', '9560', 'Simon', 'Schuster', '+43 699 2345678', 1, 'Bei Garage abstellen', '2025-05-22', '2025-05-21', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Waldstraße 6', 'Baden', '2500', 'Nina', 'Wiener', '+43 676 3456789', 1, 'Lieferung hinterlegen', '2025-05-19', '2025-05-18', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Alpenstraße 14', 'Klagenfurt', '9020', 'Mario', 'Kunz', '+43 664 4567890', 1, 'Tor bitte schließen', '2025-05-16', '2025-05-15', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Donaugasse 21', 'Krems', '3500', 'Eva', 'Leitner', '+43 650 5678901', 1, 'Vorsicht Katze', '2025-05-13', '2025-05-12', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Seenweg 17', 'Bregenz', '6900', 'Markus', 'Wolf', '+43 699 6789012', 1, 'Blumen nicht beschädigen', '2025-05-10', '2025-05-09', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Obstweg 4', 'Amstetten', '3300', 'Laura', 'Egger', '+43 676 7890123', 1, 'Zaun ist offen', '2025-05-07', '2025-05-06', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Schlossallee 15', 'Mödling', '2340', 'Tom', 'Schmidt', '+43 664 8901234', 1, 'Bitte klopfen', '2025-05-04', '2025-05-03', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Flussgasse 19', 'Leoben', '8700', 'Julia', 'Berger', '+43 650 9012345', 1, 'Hund bitte nicht füttern', '2025-05-01', '2025-04-30', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Stadtplatz 20', 'Lienz', '9900', 'Sebastian', 'Müllner', '+43 699 0123456', 1, 'Lieferung ins Carport', '2025-04-28', '2025-04-27', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Grabenweg 9', 'Hallein', '5400', 'Johanna', 'Binder', '+43 676 1234568', 1, 'Bitte Rücksicht auf Kinder', '2025-04-25', '2025-04-24', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Talstraße 3', 'Zwettl', '3910', 'Fabian', 'Hofer', '+43 664 2345679', 1, 'Klingeln zweimal', '2025-04-22', '2025-04-21', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Weinweg 18', 'Stockerau', '2000', 'Isabella', 'Fuchs', '+43 650 3456780', 1, 'Durchfahrt freihalten', '2025-04-19', '2025-04-18', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Parkstraße 10', 'Traun', '4050', 'Matthias', 'Huber', '+43 699 4567891', 1, 'An Garage lehnen', '2025-04-16', '2025-04-15', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Mühlweg 5', 'Vöcklabruck', '4840', 'Verena', 'Schwarz', '+43 676 5678902', 1, 'Nicht beim Nachbarn abgeben', '2025-04-13', '2025-04-12', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Rathausplatz 11', 'Waidhofen', '3340', 'Alexander', 'Lang', '+43 664 6789013', 1, 'Klingel kaputt', '2025-04-10', '2025-04-09', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Kirchweg 7', 'Judenburg', '8750', 'Carina', 'Mayr', '+43 650 7890124', 1, 'Bitte nicht schellen', '2025-04-07', '2025-04-06', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Dorfplatz 2', 'Eferding', '4070', 'Leon', 'Koller', '+43 699 8901235', 1, 'Blumen gießen', '2025-04-04', '2025-04-03', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Bergstraße 8', 'Bad Ischl', '4820', 'Nadine', 'Kaiser', '+43 676 9012346', 1, 'Briefkasten nutzen', '2025-04-01', '2025-03-31', (SELECT user_id FROM users ORDER BY random() LIMIT 1)),
    (gen_random_uuid(), 'Seegasse 13', 'Hermagor', '9620', 'Daniel', 'Schuster', '+43 664 0123457', 1, 'Hinter Haus abstellen', '2025-03-29', '2025-03-28', (SELECT user_id FROM users ORDER BY random() LIMIT 1));




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
WHERE ro.rn <= FLOOR(random() * 5 + 1);

WITH updated_items AS (
    SELECT
        oi.order_item_id,
        FLOOR(random() * 4 + 1)::int AS quantity,
        p.price
    FROM order_item oi
             JOIN products p ON oi.product_id = p.product_id
)
UPDATE order_item
SET
    quantity = updated_items.quantity,
    price = updated_items.quantity * updated_items.price ::numeric(10, 2)
FROM updated_items
WHERE order_item.order_item_id = updated_items.order_item_id;

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