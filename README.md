CREATE TABLE users (id name age city)

INSERT INTO users VALUES 1 john 25 newyork

UPDATE users age 28 id 1 

SELECT * FROM users

DELETE FROM users WHERE id = 1

--------------------------------------

CREATE TABLE products (id name price category)

INSERT INTO products VALUES 1 woodenTable 50 furniture

SELECT * FROM products

UPDATE products price 90 id 1 

DELETE from products where id = 1

-------------------------------------


CREATE TABLE orders (id user_id product_id quantity)

INSERT INTO orders VALUES 1 1 1 2

SELECT * FROM orders

update orders quantity 5 id 1

DELETE from orders where id = 1
