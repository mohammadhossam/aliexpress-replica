package com.aliexpress.merchantauthenticationservice.repositories;

import com.aliexpress.merchantauthenticationservice.models.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    @Query(value = "CALL public.register_merchant(:first_name, :last_name, :tax_number, :email, :phone_number, :birthdate, :address, :password, :role, null)", nativeQuery = true)
    Boolean registerMerchant(@Param("first_name") String firstName,
                          @Param("last_name") String lastName,
                          @Param("tax_number") String taxNumber,
                          @Param("email") String email,
                          @Param("phone_number") String phone,
                          @Param("birthdate") Date birthdate,
                          @Param("address") String address,
                          @Param("password") String password,
                          @Param("role") String role);

    @Query(value = "CALL public.find_merchant_by_email(:email, null, null, null, null, null, null, null, null, null, null)", nativeQuery = true)
    Optional<Merchant> findMerchantByEmail(@Param("email") String email);
    @Query(value = "CALL public.find_merchant_by_id(:id, null, null, null, null, null, null, null, null, null, null)", nativeQuery = true)
    Optional<Merchant> findMerchantById(@Param("id") int id);

}
