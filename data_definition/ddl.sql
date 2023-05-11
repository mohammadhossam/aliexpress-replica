create
database aliexpress;

-- DROP TABLE Product CASCADE;
-- DROP TABLE Merchant CASCADE;
-- DROP TABLE Buyer CASCADE;
-- INSERT INTO Buyer (first_name, last_name, email, phone_number, birthdate, address, password, role)
-- VALUES ('John', 'Doe', 'johndoe@example.com', '1234567890', '1990-01-01', '123 Main St, Anytown USA', 'password123', 'buyer');

create table Buyer(
    id           serial primary key,
    first_name   varchar(50)         not null,
    last_name    varchar(50)         not null,
    email        varchar(100) unique not null,
    phone_number varchar(20) unique  not null,
    birthdate    date                not null,
    address      varchar(200)        not null,
    password     varchar(250)        not null,
    role         varchar(100)        not null
);

CREATE TABLE Merchant
(
    id           serial primary key,
    first_name   varchar(50)         not null,
    last_name    varchar(50)         not null,
    email        varchar(100) unique not null,
    tax_number   varchar(20) unique  not null,
    phone_number varchar(20) unique  not null,
    birthdate    date                not null,
    address      varchar(200)        not null,
    password     varchar(250)        not null,
    role         varchar(100)        not null
);


CREATE TABLE Product
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    price       DECIMAL(10, 2) NOT NULL,
    category    VARCHAR(50) NOT NULL,
    amount      INTEGER NOT NULL,
    merchant_id INTEGER NOT NULL,
    CONSTRAINT fk_merchant
        FOREIGN KEY (merchant_id)
            REFERENCES Merchant (id)
);

