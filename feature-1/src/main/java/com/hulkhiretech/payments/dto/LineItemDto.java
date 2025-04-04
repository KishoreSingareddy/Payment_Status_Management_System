package com.hulkhiretech.payments.dto;

import lombok.Data;

@Data
public class LineItemDto {
	
 private int quantity;
 private String currency;
 private String productName;
 //@Min(value =1, message=" unitAmont must be greater than 0")
 private int unitAmount;
}
