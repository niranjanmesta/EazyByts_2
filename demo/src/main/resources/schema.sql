CREATE TABLE stock (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    stock_name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL
);

CREATE TABLE portfolio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    stock_name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL
);
