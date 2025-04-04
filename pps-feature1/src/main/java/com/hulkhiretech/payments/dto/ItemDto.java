package com.hulkhiretech.payments.dto;

import lombok.Data;

@Data
public class ItemDto {

	private int quantity;
	private String currency;
	private String productName;
	private int unitAmount;

}
