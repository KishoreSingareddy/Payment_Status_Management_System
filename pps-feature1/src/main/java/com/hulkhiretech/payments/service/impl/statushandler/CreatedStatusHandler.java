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
public class CreatedStatusHandler implements TxnStatusHandler{
	
	private final TransactionDao transactionDao;

	@Override
	public TransactionDto processStatus(TransactionDto txnDto) {
		
		log.info(" Processing CREATED Status: {}", txnDto);
		
		 txnDto=transactionDao.createTransaction(txnDto);
		 
			log.info("Created Txn in DB||txnDto:" + txnDto);

		 return txnDto;
	}
}
