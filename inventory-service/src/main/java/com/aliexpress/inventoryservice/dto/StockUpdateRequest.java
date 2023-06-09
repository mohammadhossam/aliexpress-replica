package com.aliexpress.inventoryservice.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockUpdateRequest {
  String[] ids;
  int[] amount;
}