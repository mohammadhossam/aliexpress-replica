-- Procedure for registering a new buyer
CREATE OR REPLACE PROCEDURE register_buyer(IN first_name varchar(50), IN last_name varchar(50), IN email varchar(100),
                                           IN phone_number varchar(20), IN birthdate date, IN address varchar(200),
                                           IN password varchar(250), IN role varchar(100), OUT result boolean)
AS
$$
DECLARE
    new_id integer;
BEGIN
    -- Insert the new buyer into the Buyer table
    INSERT INTO Buyer(first_name, last_name, email, password, phone_number, birthdate, address, role)
    VALUES (first_name, last_name, email, password, phone_number, birthdate, address, role);

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
                                              IN password varchar(60), OUT result boolean)
AS
$$
DECLARE
    new_id integer;
BEGIN
    -- Insert the new buyer into the Merchant table
    INSERT INTO Merchant(first_name, last_name, email, password, tax_number, phone_number, birthdate, address)
    VALUES (first_name, last_name, email, password, tax_number, phone_number, birthdate, address);

    result := true;
EXCEPTION
    -- If there is an error, set the result to false and rollback the transaction
    WHEN OTHERS THEN
        result := false;
        RAISE NOTICE 'Error while registering merchant: %', SQLERRM;
        ROLLBACK;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE edit_buyer_profile(
    OUT success BOOLEAN,
    OUT reason VARCHAR(200),
    IN p_buyer_id INTEGER,
    IN p_first_name VARCHAR(50) = NULL,
    IN p_last_name VARCHAR(50) = NULL,
    IN p_email VARCHAR(100) = NULL,
    IN p_phone_number VARCHAR(20) = NULL,
    IN p_birthdate date = NULL,
    IN p_address VARCHAR(200) = NULL,
    In p_password varchar(250)  = NULL
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE Buyer
    SET first_name   = COALESCE(p_first_name, first_name),
        last_name    = COALESCE(p_last_name, last_name),
        email        = COALESCE(p_email, email),
        phone_number = COALESCE(p_phone_number, phone_number),
        birthdate    = COALESCE(p_birthdate, birthdate),
        address      = COALESCE(p_address, address),
        password     = COALESCE(p_password, password)
    WHERE id = p_buyer_id;

    IF FOUND THEN
        success := TRUE;
        reason := 'Success';
    ELSE
        success := FALSE;
        reason := 'No record found for buyerId: ' || p_buyer_id;
        RETURN;
    END IF;

EXCEPTION
    WHEN OTHERS THEN
        success := FALSE;
        reason := SQLERRM;
END;
$$;

CREATE OR REPLACE PROCEDURE edit_merchant_profile(
    OUT success BOOLEAN,
    OUT reason VARCHAR(200),
    IN p_merchant_id INTEGER,
    IN p_first_name VARCHAR(50) = NULL,
    IN p_last_name VARCHAR(50) = NULL,
    IN p_email VARCHAR(100) = NULL,
    IN p_phone_number VARCHAR(20) = NULL,
    IN p_birthdate date = NULL,
    IN p_address VARCHAR(200) = NULL,
    In p_password varchar(250)  = NULL,
    In p_tax_number varchar(20)  = NULL
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE Merchant
    SET first_name   = COALESCE(p_first_name, first_name),
        last_name    = COALESCE(p_last_name, last_name),
        email        = COALESCE(p_email, email),
        phone_number = COALESCE(p_phone_number, phone_number),
        birthdate    = COALESCE(p_birthdate, birthdate),
        address      = COALESCE(p_address, address),
        password     = COALESCE(p_password, password),
        tax_number   = COALESCE(p_tax_number, tax_number)
    WHERE id = p_merchant_id;

    IF FOUND THEN
        success := TRUE;
        reason := 'Success';
    ELSE
        success := FALSE;
        reason := 'No record found for merchantId: ' || p_merchant_id;
        RETURN;
    END IF;

EXCEPTION
    WHEN OTHERS THEN
        success := FALSE;
        reason := SQLERRM;
END;
$$;

CREATE OR REPLACE PROCEDURE create_product(
    IN p_id VARCHAR(50),
    IN p_quantity INTEGER
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO Inventory (id, quantity)
    VALUES (p_id, p_quantity);
END;
$$;

CREATE OR REPLACE PROCEDURE delete_product(
    IN p_product_id VARCHAR(50)
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    -- Delete product with specified id
    DELETE FROM Inventory WHERE id = p_product_id;
END;
$$;

CREATE OR REPLACE PROCEDURE update_product(
    IN p_product_id VARCHAR(50),
    IN p_quantity INTEGER
)
    LANGUAGE plpgsql
AS
$$
BEGIN
        -- Update the product data
        UPDATE Inventory
        SET quantity= p_quantity
        WHERE id = p_product_id;

        -- Commit the transaction
    EXCEPTION
        WHEN OTHERS THEN
            -- Rollback the transaction if there is an exception
            RAISE;
END;
$$;

CREATE OR REPLACE PROCEDURE get_product_data(
    IN p_product_id VARCHAR(50),
    OUT p_quantity INTEGER
)
    LANGUAGE plpgsql
AS
$$
BEGIN
    SELECT quantity
    INTO p_quantity
    FROM Inventory
    WHERE id = p_product_id;
END;
$$;

CREATE OR REPLACE PROCEDURE increment_products(ids text[], amount integer[]) AS
$$
DECLARE
  i INTEGER;
  v_stock INTEGER;
BEGIN
  FOR i IN array_lower(ids, 1)..array_upper(ids, 1) LOOP
    BEGIN
        SELECT quantity INTO STRICT v_stock FROM Inventory WHERE id = ids[i];
        UPDATE Inventory SET quantity = quantity + amount[i] WHERE id = ids[i];
    EXCEPTION
        WHEN OTHERS THEN
            -- Rollback the transaction if there is an exception
            RAISE;
    END;
  END LOOP;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE decrement_products(ids text[], amount integer[]) AS
$$
DECLARE
  i INTEGER;
  v_stock INTEGER;
BEGIN
  FOR i IN array_lower(ids, 1)..array_upper(ids, 1) LOOP
    BEGIN
        SELECT quantity INTO STRICT v_stock FROM Inventory WHERE id = ids[i];
        IF v_stock < amount[i] THEN
            RAISE EXCEPTION 'Not enough stock available';
        END IF;
        UPDATE Inventory SET quantity = quantity - amount[i] WHERE id = ids[i];
    EXCEPTION
        WHEN OTHERS THEN
            -- Rollback the transaction if there is an exception
            RAISE;
    END;
  END LOOP;
END;
$$
LANGUAGE plpgsql;