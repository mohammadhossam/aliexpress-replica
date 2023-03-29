create
database aliexpress;

create table Buyer(
    id           serial primary key,
    first_name   varchar(50)         not null,
    last_name    varchar(50)         not null,
    email        varchar(100) unique not null,
    phone_number varchar(20) unique  not null,
    birthdate    date                not null,
    address      varchar(200)        not null
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
    address      varchar(200)        not null
);


CREATE TABLE BuyerPassword
(
    id       INTEGER PRIMARY KEY,
    password VARCHAR(60) NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY (id)
            REFERENCES Buyer (id)
);

CREATE TABLE MerchantPassword
(
    id       INTEGER PRIMARY KEY,
    password VARCHAR(60) NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY (id)
            REFERENCES Merchant (id)
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

