CREATE TABLE customer
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50),
    email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE customer_address
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    customer_id BIGINT REFERENCES customer (id) ON DELETE CASCADE
);

CREATE TABLE product
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(10, 2) NOT NULL,
    status VARCHAR(50),
    quantity_on_hand INT NOT NULL
);

CREATE TABLE orders
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    shipping_charge NUMERIC(10, 2),
    total_price NUMERIC(10, 2),
    shipped BOOLEAN DEFAULT FALSE,
    customer_id BIGINT REFERENCES customer (id) ON DELETE CASCADE,
    shipping_address_id BIGINT REFERENCES customer_address (id) ON DELETE SET NULL
);

CREATE TABLE order_products
(
    order_id BIGINT REFERENCES orders (id) ON DELETE CASCADE,
    product_id BIGINT REFERENCES product (id) ON DELETE CASCADE,
    PRIMARY KEY (order_id, product_id)
);

ALTER TABLE customer ALTER COLUMN phone_number TYPE VARCHAR(50);
