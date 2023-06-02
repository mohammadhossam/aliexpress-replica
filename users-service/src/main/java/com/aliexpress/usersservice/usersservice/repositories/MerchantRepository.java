package com.aliexpress.usersservice.usersservice.repositories;

import com.aliexpress.usersservice.usersservice.models.Merchant;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Map;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    @Modifying
    @Transactional
    @Query(value = "CALL public.edit_merchant_profile(null,null,:merchantId,:firstName,:lastName,:email,:phone,:birthdate,:address,:password,:taxNumber)", nativeQuery = true)
    Map<String, Object> updateMerchant(
            @Param("merchantId") Integer merchantId,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("birthdate") Date birthdate,
            @Param("address") String address,
            @Param("password") String password,
            @Param("taxNumber") String taxNumber);
}
