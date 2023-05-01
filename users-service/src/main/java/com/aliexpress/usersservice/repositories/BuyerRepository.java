package com.aliexpress.usersservice.repositories;

import com.aliexpress.usersservice.models.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {
    @Query(value = "CALL public.register_buyer(:first_name, :last_name, :email, :phone_number, :birthdate, :address, :hashed_password, null)", nativeQuery = true)
    Boolean registerBuyer(@Param("first_name") String firstName,
                          @Param("last_name") String lastName,
                          @Param("email") String email,
                          @Param("phone_number") String phone,
                          @Param("birthdate") Date birthdate,
                          @Param("address") String address,
                          @Param("hashed_password") String hashedPassword);
}
