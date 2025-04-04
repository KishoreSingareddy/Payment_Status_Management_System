package com.hulkhiretech.payments.dao.interfaces;

import com.hulkhiretech.payments.dto.TransactionDto;

public interface TransactionDao {
	public TransactionDto createTransaction(TransactionDto txnDto);
	
	public TransactionDto getTransactionByReference(String txnReference);

	public TransactionDto updateTransactionStatusDetails(TransactionDto dto);
	
	public TransactionDto getTransactionByProviderReference(String providerReference);


}
