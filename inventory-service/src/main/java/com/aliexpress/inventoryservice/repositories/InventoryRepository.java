package com.aliexpress.inventoryservice.repositories;

import com.aliexpress.inventoryservice.dto.Item;
import com.aliexpress.inventoryservice.models.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

public interface InventoryRepository extends JpaRepository<Inventory, String> {
    @Transactional
    @Modifying
    @Query(value = "CALL public.create_product(:p_id, :p_quantity)", nativeQuery = true)
    void createInventory(@Param("p_id") String p_id, @Param("p_quantity") int p_quantity);

    @Query(value = "CALL public.get_product_data(:p_product_id, null)", nativeQuery = true)
    int getInventoryById(@Param("p_product_id") String p_product_id);
    @Transactional
    @Modifying
    @Query(value = "CALL public.update_product(:p_product_id, :p_quantity)", nativeQuery = true)
    void updateInventory(@Param("p_product_id") String p_product_id,
                         @Param("p_quantity") int p_quantity);
    @Transactional()
    @Modifying
    @Query(value = "CALL public.delete_product(:p_product_id)", nativeQuery = true)
    void deleteInventory(@Param("p_product_id") String p_product_id);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Modifying
    @Query(value = "CALL public.decrement_product_stock(:p_product_id,:p_quantity)", nativeQuery = true)
    void decrementStock(@Param("p_product_id") String p_product_id,
                        @Param("p_quantity") int p_quantity);
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Modifying
    @Query(value = "CALL public.increment_product_stock(:p_product_id,:p_quantity)", nativeQuery = true)
    void incrementStock(@Param("p_product_id") String p_product_id,
                        @Param("p_quantity") int p_quantity);
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Modifying
    @Query(value = "CALL public.decrement_products(:ids,:amount)", nativeQuery = true)
    void decrementProducts(@Param("ids") String[] p_product_id,
                        @Param("amount") int[] p_quantity);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Modifying
    @Query(value = "CALL public.increment_products(:ids,:amount)", nativeQuery = true)
    void incrementProducts(@Param("ids") String[] p_product_id,
                           @Param("amount") int[] p_quantity);
//    @Transactional(isolation = Isolation.SERIALIZABLE)
//    @Modifying
//    @Query(value = "CALL public.decrement_products(:items)", nativeQuery = true)
//    void decrementProducts(@Param("items") List<Item>  items);
}