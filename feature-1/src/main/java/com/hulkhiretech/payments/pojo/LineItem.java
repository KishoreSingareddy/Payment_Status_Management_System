package com.hulkhiretech.payments.pojo;

import lombok.Data;

@Data
public class LineItem {
	
 private int quantity;
 private String currency;
 private String productName;
 //@Min(value =1, message=" unitAmont must be greater than 0")
 private int unitAmount;

}
