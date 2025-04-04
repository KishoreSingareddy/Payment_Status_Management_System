package com.hulkhiretech.payments.service.interfaces;

import com.hulkhiretech.payments.dto.CreatePaymentDto;
import com.hulkhiretech.payments.dto.PaymentDto;

public interface PaymentService {

	public PaymentDto createPayment(CreatePaymentDto createPaymentDto);

	public PaymentDto getPayment(String id);
	public PaymentDto expirePayment(String id);


}
