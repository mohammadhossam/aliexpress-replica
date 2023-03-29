CREATE OR REPLACE PROCEDURE authenticate_buyer(IN email varchar(100), IN hashed_password varchar(60),
                                               OUT result boolean, OUT buyer_id integer)
AS
$$
BEGIN
    SELECT id, password = hashed_password
    INTO buyer_id, result
    FROM Buyer
             JOIN BuyerPassword ON Buyer.id = BuyerPassword.id
    WHERE Buyer.email = authenticate_buyer.email;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE authenticate_merchant(IN email varchar(100), IN hashed_password varchar(60),
                                                  OUT result boolean, OUT merchant_id integer)
AS
$$
BEGIN
    SELECT id, password = hashed_password
    INTO merchant_id, result
    FROM Merchant
             JOIN BuyerPassword ON Merchant.id = BuyerPassword.id
    WHERE Merchant.email = authenticate_merchant.email;
END;
$$ LANGUAGE plpgsql;

-- Procedure for registering a new buyer
CREATE OR REPLACE PROCEDURE register_buyer(IN first_name varchar(50), IN last_name varchar(50), IN email varchar(100),
                                           IN phone_number varchar(20), IN birthdate date, IN address varchar(200),
                                           IN hashed_password varchar(60), OUT result boolean)
AS
$$
DECLARE
    new_id integer;
BEGIN
    -- Insert the new buyer into the Buyer table
    INSERT INTO Buyer(first_name, last_name, email, phone_number, birthdate, address)
    VALUES (first_name, last_name, email, phone_number, birthdate, address)
    RETURNING id INTO new_id;

    -- Insert the new buyer's password into the BuyerPassword table
    INSERT INTO BuyerPassword(id, password)
    VALUES (new_id, hashed_password);

    -- Set the result to true to indicate success
    result := true;
EXCEPTION
    -- If there is an error, set the result to false and rollback the transaction
    WHEN OTHERS THEN
        result := false;
        RAISE NOTICE 'Error while registering buyer: %', SQLERRM;
        ROLLBACK;
END;
$$ LANGUAGE plpgsql;

-- Procedure for registering a new buyer
CREATE OR REPLACE PROCEDURE register_merchant(IN first_name varchar(50), IN last_name varchar(50),
                                              IN email varchar(100), IN tax_number varchar(20),
                                              IN phone_number varchar(20), IN birthdate date, IN address varchar(200),
                                              IN hashed_password varchar(60), OUT result boolean)
AS
$$
DECLARE
    new_id integer;
BEGIN
    -- Insert the new buyer into the Merchant table
    INSERT INTO Merchant(first_name, last_name, email, tax_number, phone_number, birthdate, address)
    VALUES (first_name, last_name, email, tax_number, phone_number, birthdate, address)
    RETURNING id INTO new_id;

    -- Insert the new merchant's password into the MerchantPassword table
    INSERT INTO merchantpassword(id, password)
    VALUES (new_id, hashed_password);

    -- Set the result to true to indicate success
    result := true;
EXCEPTION
    -- If there is an error, set the result to false and rollback the transaction
    WHEN OTHERS THEN
        result := false;
        RAISE NOTICE 'Error while registering merchant: %', SQLERRM;
        ROLLBACK;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE create_product(
    IN p_merchant_id INTEGER,
    IN p_name VARCHAR(100),
    IN p_description VARCHAR(500),
    IN p_price DECIMAL(10, 2),
    IN p_category VARCHAR(50),
    IN p_amount INTEGER
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO Product (merchant_id, name, description, price, category, amount)
    VALUES (p_merchant_id, p_name, p_description, p_price, p_category, p_amount);
END;
$$;

CREATE OR REPLACE PROCEDURE delete_product(
    IN p_product_id INTEGER
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    -- Delete product with specified id
    DELETE FROM Product WHERE id = p_product_id;
END;
$$;

CREATE OR REPLACE PROCEDURE decrement_product_stock(
    IN p_product_id INTEGER,
    IN p_quantity INTEGER
)
    LANGUAGE plpgsql
AS
$$
DECLARE
    v_stock INTEGER;
BEGIN
    BEGIN
        -- Start a serializable transaction
        START TRANSACTION ISOLATION LEVEL SERIALIZABLE;

        -- Check if there is enough stock available
        SELECT amount INTO STRICT v_stock FROM Product WHERE id = p_product_id;
        IF v_stock < p_quantity THEN
            RAISE EXCEPTION 'Not enough stock available';
        END IF;

        -- Decrement the stock
        UPDATE Product SET amount = amount - p_quantity WHERE id = p_product_id;

        -- Commit the transaction
        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            -- Rollback the transaction if there is an exception
            ROLLBACK;
            RAISE;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE update_product(
    IN p_product_id INTEGER,
    IN p_name VARCHAR(100),
    IN p_description VARCHAR(500),
    IN p_price DECIMAL(10, 2),
    IN p_category VARCHAR(50),
    IN p_amount INTEGER
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    BEGIN
        -- Start a transaction
        START TRANSACTION;

        -- Update the product data
        UPDATE Product
        SET name        = p_name,
            description = p_description,
            price       = p_price,
            category    = p_category,
            amount      = p_amount
        WHERE id = p_product_id;

        -- Commit the transaction
        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            -- Rollback the transaction if there is an exception
            ROLLBACK;
            RAISE;
    END;
END;
$$;

CREATE OR REPLACE PROCEDURE get_product_data(
    IN p_product_id INTEGER,
    OUT p_name VARCHAR(100),
    OUT p_description VARCHAR(500),
    OUT p_price DECIMAL(10, 2),
    OUT p_category VARCHAR(50),
    OUT p_amount INTEGER,
    OUT p_merchant_id INTEGER
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    SELECT name, description, price, category, amount, merchant_id
    INTO p_name, p_description, p_price, p_category, p_amount, p_merchant_id
    FROM Product
    WHERE id = p_product_id;
END;
$$;

CREATE OR REPLACE PROCEDURE edit_buyer_profile(
    IN p_buyer_id INTEGER,
    IN p_first_name VARCHAR(50),
    IN p_last_name VARCHAR(50),
    IN p_email VARCHAR(100),
    IN p_phone_number VARCHAR(20),
    IN p_address VARCHAR(200)
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE Buyer
    SET first_name   = p_first_name,
        last_name    = p_last_name,
        email        = p_email,
        phone_number = p_phone_number,
        address      = p_address
    WHERE id = p_buyer_id;
END;
$$;

CREATE OR REPLACE PROCEDURE edit_merchant_profile(
    IN p_merchant_id INTEGER,
    IN p_first_name VARCHAR(50),
    IN p_last_name VARCHAR(50),
    IN p_email VARCHAR(100),
    IN p_tax_number VARCHAR(20),
    IN p_phone_number VARCHAR(20),
    IN p_birthdate DATE,
    IN p_address VARCHAR(200)
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE Merchant
    SET first_name   = p_first_name,
        last_name    = p_last_name,
        email        = p_email,
        tax_number   = p_tax_number,
        phone_number = p_phone_number,
        birthdate    = p_birthdate,
        address      = p_address
    WHERE id = p_merchant_id;
END;
$$;

