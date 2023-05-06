package com.aliexpress.usersservice.repositories;

import com.aliexpress.usersservice.models.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {
    @Query(value = "CALL public.register_buyer(:first_name, :last_name, :email, :phone_number, :birthdate, :address, :password, :role, null)", nativeQuery = true)
    Boolean registerBuyer(@Param("first_name") String firstName,
                          @Param("last_name") String lastName,
                          @Param("email") String email,
                          @Param("phone_number") String phone,
                          @Param("birthdate") Date birthdate,
                          @Param("address") String address,
                          @Param("password") String password,
                          @Param("role") String role);

    @Query(value = "CALL public.find_buyer_by_email(:email, null, null, null, null, null, null, null, null, null)", nativeQuery = true)
    Optional<Buyer> findBuyerByEmail(@Param("email") String email);
}
