package com.hulkhiretech.payments.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.hulkhiretech.payments.constants.ErrorCodeEnum;
import com.hulkhiretech.payments.constants.TransactionStatusEnum;
import com.hulkhiretech.payments.dto.TransactionDto;
import com.hulkhiretech.payments.exception.ProcessingException;
import com.hulkhiretech.payments.service.PaymentStatusFactory;
import com.hulkhiretech.payments.service.interfaces.PaymentStatusService;
import com.hulkhiretech.payments.service.interfaces.TxnStatusHandler;

import lombok.extern.slf4j.Slf4j;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Slf4j
@Service
public class PaymentStatusServiceImpl implements PaymentStatusService{
	
	private final PaymentStatusFactory paymentStatusFactory;
	
	

	@Override
	public TransactionDto processStatus(TransactionDto txnDto) {
		// TODO Auto-generated method stub
		
log.info("*******Received txnDto:" + txnDto + "||paymentStatusFactory:" + paymentStatusFactory);
		
		TransactionStatusEnum statusEnum = TransactionStatusEnum.getByName(
				txnDto.getTxnStatus());
		
		if (statusEnum == null) {
			log.error("Invalid status received||status:{}", txnDto.getTxnStatus());
			
			throw new ProcessingException(
					ErrorCodeEnum.INVALID_PAYMENT_STATUS.getErrorCode(), 
					ErrorCodeEnum.INVALID_PAYMENT_STATUS.getErrorMessage(), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		TxnStatusHandler statusHandler = paymentStatusFactory.getHandler(statusEnum);
		if (statusHandler == null) {
			throw new ProcessingException(
					ErrorCodeEnum.NO_STATUS_HANDLE_FOUND.getErrorCode(), 
					ErrorCodeEnum.NO_STATUS_HANDLE_FOUND.getErrorMessage(), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		TransactionDto responseDTO = statusHandler.processStatus(txnDto);
		log.info("responseDTO:" + responseDTO);
		
		return responseDTO;
	}

}
