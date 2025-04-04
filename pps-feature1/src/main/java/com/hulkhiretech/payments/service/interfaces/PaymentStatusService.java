package com.hulkhiretech.payments.service.interfaces;

import com.hulkhiretech.payments.dto.TransactionDto;

public interface PaymentStatusService {
	public TransactionDto processStatus(TransactionDto txnDto);
}
