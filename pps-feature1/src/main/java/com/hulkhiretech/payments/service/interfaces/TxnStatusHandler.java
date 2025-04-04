package com.hulkhiretech.payments.service.interfaces;

import com.hulkhiretech.payments.dto.TransactionDto;

public interface TxnStatusHandler {
	public TransactionDto processStatus(TransactionDto txnDto);
}
