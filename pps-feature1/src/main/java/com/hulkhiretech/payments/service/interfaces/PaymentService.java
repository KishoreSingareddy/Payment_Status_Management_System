package com.hulkhiretech.payments.service.interfaces;

import com.hulkhiretech.payments.dto.InitiatePaymentDto;
import com.hulkhiretech.payments.dto.TransactionDto;

public interface PaymentService {
	public TransactionDto createPayment(TransactionDto transactionDto);
	public  TransactionDto initiatePayment(String txnReference, InitiatePaymentDto reqDto);
}