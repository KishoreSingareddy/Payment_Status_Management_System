package com.hulkhiretech.payments.dto;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class CheckoutSessionCompletedData {

	public String id;
	public String status;
	@SerializedName("payment_status")
	public String paymentStatus;
}
