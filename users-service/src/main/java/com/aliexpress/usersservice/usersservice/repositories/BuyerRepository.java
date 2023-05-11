package com.aliexpress.usersservice.usersservice.repositories;

import com.aliexpress.usersservice.usersservice.models.Buyer;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Optional;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {
    @Modifying
    @Transactional
    @Query(value = "CALL public.edit_buyer_profile(null,:buyerId,:firstName,:lastName,:email,:phone,:birthdate,:address,:password)", nativeQuery = true)
    Boolean updateBuyer(
            @Param("buyerId") Integer buyerId,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("birthdate") Date birthdate,
            @Param("address") String address,
            @Param("password") String password);
}
