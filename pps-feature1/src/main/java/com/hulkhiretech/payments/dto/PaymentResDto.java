package com.hulkhiretech.payments.dto;

import lombok.Data;

@Data
public class PaymentResDto {
	private String id;
	private String url;
	private String status;
	private String paymentStatus;
}
