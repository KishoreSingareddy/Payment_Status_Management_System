package com.hulkhiretech.payments.dto;

import com.hulkhiretech.payments.pojo.StripeData;

import lombok.Data;

@Data
public class StripeEventDto {
	private String id;
	private String type;
	private StripeData data;

}
