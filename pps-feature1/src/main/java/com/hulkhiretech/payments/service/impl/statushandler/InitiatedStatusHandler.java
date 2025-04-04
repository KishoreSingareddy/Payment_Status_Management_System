package com.hulkhiretech.payments.service.impl.statushandler;

import org.springframework.stereotype.Service;

import com.hulkhiretech.payments.dao.interfaces.TransactionDao;
import com.hulkhiretech.payments.dto.TransactionDto;
import com.hulkhiretech.payments.service.interfaces.TxnStatusHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class InitiatedStatusHandler implements TxnStatusHandler{
	private final TransactionDao transactionDao;


	@Override
	public TransactionDto processStatus(TransactionDto txnDto) {
		// TODO Auto-generated method stub
log.info("Processing INITIATED status||txnDto:" + txnDto);
		
		transactionDao.updateTransactionStatusDetails(txnDto);
		
		log.info("Updated Txn in DB||txnDto:" + txnDto);
		
		return txnDto;
		
	}
}