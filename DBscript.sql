DROP TABLE IF EXISTS orders_products; 
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS orders; 
DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS customer; 

	
CREATE TABLE 
	customer (
		customer_id serial PRIMARY KEY NOT NULL,
		name VARCHAR(255) NOT NULL,
		email VARCHAR(255) UNIQUE NOT NULL,
		phone CHAR(12) NOT NULL,
		CONSTRAINT chk_phone CHECK (phone not like '%[^0-9]%'),
		age SMALLINT DEFAULT 99 CHECK (age>= 18), 
		address VARCHAR(255) NOT NULL,
		city VARCHAR(255),
		postal_code INT CHECK (postal_code>0),
		country VARCHAR(255),
		consent_status BOOLEAN NOT NULL,
		is_profile_active BOOLEAN NOT NULL,
		date_profile_created DATE DEFAULT CURRENT_DATE NOT NULL,
		date_profile_deactivated DATE,
		reason_for_deactivation VARCHAR(255),
		notes TEXT);
		
		
CREATE TABLE 
	address (
		address_id SERIAL PRIMARY KEY NOT NULL,
		customer_id INT NOT NULL,
		address VARCHAR(255),
		city VARCHAR(255) NOT NULL, 
		province VARCHAR(255),
		state_UK VARCHAR(255),
		postal_code INT CHECK (postal_code > 0),
		country VARCHAR(255) NOT NULL,
		CONSTRAINT fk_customer_id FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON UPDATE CASCADE ON DELETE CASCADE
		);
		
			
CREATE TABLE 
	orders (
		order_id SERIAL PRIMARY KEY,
		customer_id INT NOT NULL,
		is_order_completed BOOLEAN NOT NULL,
		is_order_payed BOOLEAN NOT NULL,
		date_of_order DATE DEFAULT CURRENT_DATE NOT NULL,
		date_order_completed DATE,
		CONSTRAINT fk_customer_id FOREIGN KEY(customer_id) REFERENCES customer(customer_id) ON UPDATE CASCADE ON DELETE CASCADE);
		
		
CREATE TABLE 
	product (
		product_id SERIAL PRIMARY KEY,
		product_name VARCHAR(255) NOT NULL,
		available_quantity INT NOT NULL,
		product_type VARCHAR(255) NOT NULL,
		price_without_VAT FLOAT NOT NULL,
		price_with_VAT FLOAT NOT NULL,
		is_product_in_stock BOOLEAN NOT NULL,
		warehouse VARCHAR(255) NOT NULL);
		
			
CREATE TABLE 
	orders_products (
	order_id INT REFERENCES orders (order_id) ON UPDATE CASCADE ON DELETE CASCADE,
		product_id INT REFERENCES product(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
		ordered_quantity INT NOT NULL,
		CONSTRAINT order_product_pkey PRIMARY KEY (order_id, product_id));	

----------------------------------------------------------------------------------

INSERT INTO 
	customer (name, email, phone, age, address, city, postal_code, country, consent_status, is_profile_active) 		 			 
VALUES 
	('Mark', 'mahjrk@abv.bg', +359888444561, 25, 'Bulgaria', 'Sofia', 359, 'BG', true, true), 
	('Alice', 'ablice@jit.bg', +359878448561, 26, 'Greece', 'Athens', 478, 'Greece', false, true),
	('Bob', 'boghjfb@fsdg.bg', +359878447561, 46, 'Bulgaria', 'Sliven', 368, 'Bulgaria', true, false),
	('Clie', 'cadhjrlie@gmail.bg', +3598444561, 64, 'Bulgaria', 'Ruse', 258, 'Bulgaria', true, true),
	('Pesho', 'chfhrlie@gmail.bg', +3596784561, 64, 'Bulgaria', 'Ruse', 258, 'Bulgaria', true, true);
	

INSERT INTO 
	address (customer_id, address, city, state_UK, postal_code, country) 	 	
VALUES 
	(2, '200 Westminster Bridge Road, London, SE1 7UT, United Kingdom', 'London', 'UK', 5211, 'United Kingdom'),
	(1, 'St Katharines Way, London, E1W 1LD, United Kingdom', 'London', 'UK', 5211, 'United Kingdom'),
	(3, '11 Blackfriars Street, Manchester, M3 5AL, United Kingdom', 'Manchester', 'UK', 8911, 'United Kingdom'),
	(4, '1 Morrison Link, Edinburgh, EH3 8DN, United Kingdom', 'Edinburgh', 'UK', 1211, 'United Kingdom'),
	(5, '5 Morrison Link, Edinburgh, EH3 8DN, United Kingdom', 'Edinburgh', 'UK', 1211, 'United Kingdom');

				
INSERT INTO 
	orders 	 	
VALUES 
 	(1, 1, false, false),
 	(2, 3, false, true),
 	(3, 2, true, true),
 	(4, 5, true, true),
 	(5, 4, false, false);
 	

INSERT INTO 
 	product	(product_name, available_quantity, product_type, price_without_VAT, price_with_VAT, is_product_in_stock, warehouse)
VALUES 
	( 'bread', 50, 'food', 1.5,  1.8, true, 'Sofia'),
	( 'chocolate', 150, 'food', 2,  2.12, true, 'Sliven'),
	( 'milk', 37, 'food', 2.2,  2.64, true, 'Karlovo'),
	( 'fish', 12, 'food', 5.2,  6.24, true, 'Burgas'),
	( 'fish', 0, 'food', 5.2,  6.24, false, 'Varna'),
	('cheese', 84, 'food', 9.8,  11.76, true, 'Burgas'),
	( 'cheese', 0, 'food', 9.8,  11.76, false, 'Velingrad'),
	( 'whater', 135, 'drink', 1,  1.25, true, 'Devin'),
	( 'whater', 150, 'drink', 1.2,  1.44, true, 'Velingrad'),
	( 'Marble Land', 49, 'wine', 27,  32.40, true, 'Ivailovgrad'),
	( 'Vino Reserve Ivaylovgrad', 49, 'wine', 18,  21.60, true, 'Ivailovgrad'),
	( 'Merlot Grand Reserve', 62, 'wine', 15,  18, true, 'Ivailovgrad'),
	( 'Villa Armira Merlot', 59, 'wine', 7,  8.40, true, 'Ivailovgrad'),
	( 'Roses Yamantievs', 0, 'wine', 12,  14.40, false, 'Sofia'),
	( 'Roses Yamantievs', 89, 'wine', 12,  14.40, true, 'Ivailovgrad'),
	( 'fresh orange', 3, 'drink', 5.2,  6.24, true, 'Sofia'),
	( 'apple', 67, 'fruit', 2.3,  2.76, true, 'Karlovo'),
	( 'strawberries', 0, 'fruit', 5.2,  6.24, false, 'Sofia'),
	( 'strawberries', 65, 'fruit', 5.2,  6.24, true, 'Ivailovgrad'),
	( 'banana', 0, 'fruit', 5.2,  6.24, false, 'Vidin');
	
		
INSERT INTO 
	orders_products	 	
VALUES 
 	(1, 1, 10),
 	(2, 2, 2),
 	(3, 3, 1),
 	(4, 4, 150),
 	(5, 5, 35);
 
 
 --------------
 
SELECT product.* FROM 
	orders_products
INNER JOIN
    product 
    ON
	orders_products.product_id = product.product_id
WHERE orders_products.order_id = 1



	