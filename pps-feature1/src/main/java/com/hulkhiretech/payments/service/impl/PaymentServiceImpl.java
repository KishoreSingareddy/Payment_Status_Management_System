package com.hulkhiretech.payments.service.impl;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.hulkhiretech.payments.constants.Constants;
import com.hulkhiretech.payments.constants.ErrorCodeEnum;
import com.hulkhiretech.payments.constants.TransactionStatusEnum;
import com.hulkhiretech.payments.dao.interfaces.TransactionDao;
import com.hulkhiretech.payments.dto.InitiatePaymentDto;
import com.hulkhiretech.payments.dto.PaymentResDto;
import com.hulkhiretech.payments.dto.TransactionDto;
import com.hulkhiretech.payments.exception.ProcessingException;
import com.hulkhiretech.payments.http.HttpRequest;
import com.hulkhiretech.payments.http.HttpServiceEngine;
import com.hulkhiretech.payments.service.interfaces.PaymentService;
import com.hulkhiretech.payments.service.interfaces.PaymentStatusService;
import com.hulkhiretech.payments.stripeprovider.CreatePaymentReq;
import com.hulkhiretech.payments.stripeprovider.PaymentRes;
import com.hulkhiretech.payments.stripeprovider.SPErrorResponse;
import com.hulkhiretech.payments.util.GsonUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public  class PaymentServiceImpl implements PaymentService {
	private final PaymentStatusService paymentStatusService;
	private final HttpServiceEngine httpServiceEngine;
	private final Gson gson;
	private final TransactionDao transactionDao;
	private final ModelMapper modelMapper;
	private final GsonUtils gsonUtils;
	
	@Value("${stripe.provider.create.payment.url}")
	private String stripeProviderCreatePaymentUrl;
	
	@Override
	public TransactionDto createPayment(TransactionDto transactionDto) {
		log.info("Received transactionDTO:" + transactionDto);

		transactionDto.setTxnStatus(TransactionStatusEnum.CREATED.getName());
		transactionDto.setTxnReference(generateTxnReference()); //unique value for this payment.
		
		transactionDto = paymentStatusService.processStatus(transactionDto);
		
		log.info("Created Payment in DB: transactionDTO:" + transactionDto);
		
		return transactionDto;
	}

	private String generateTxnReference() {
		String txnReference = UUID.randomUUID().toString();
		log.info("Generated txnReference:" + txnReference);
		return txnReference;
	}
	@Override
	public TransactionDto  initiatePayment(String txnReference, InitiatePaymentDto reqDto){
		log.info("Initiating payment txnReference:{}|reqDto:{}", 
				txnReference, reqDto);

		// Load Txn from DB based on txnReference.
		TransactionDto txnDto = transactionDao.getTransactionByReference(txnReference);
		log.info("txnDto from DB:" + txnDto);

		if(txnDto == null) {
			throw new ProcessingException(
					ErrorCodeEnum.INVALID_TXN_REFERENCE.getErrorCode(), 
					ErrorCodeEnum.INVALID_TXN_REFERENCE.getErrorMessage(),
					HttpStatus.BAD_REQUEST);
		}
		/*
		if (TransactionStatusEnum.SUCCESS.getName().equals(txnDto.getTxnStatus())) {
	        log.error("Transaction {} is already marked as SUCCESS. Returning error response.", txnReference);
	        throw new ProcessingException(
	                "10000", // Custom error code
	                "Payment already done and is success",
	                HttpStatus.BAD_REQUEST);
	    }
*/

		txnDto.setTxnStatus(TransactionStatusEnum.INITIATED.getName());
		paymentStatusService.processStatus(txnDto);

		HttpRequest httpRequest = prepareHttpRequest(reqDto);
		log.info("Prepared httpRequest:" + httpRequest);

		try {
			// Make API call to stripe provider service. RestClient
			ResponseEntity<String> httpResponse = httpServiceEngine.makeHttpCall(httpRequest);
			
			PaymentResDto paymentResponse = processResponse(httpResponse);
			
			//success update to PENDING
			txnDto.setTxnStatus(TransactionStatusEnum.PENDING.getName());
			txnDto.setProviderReference(paymentResponse.getId());
			txnDto.setUrl(paymentResponse.getUrl());
			paymentStatusService.processStatus(txnDto);
			log.info("Successfully got url & updated in DB:" + txnDto);

			return txnDto;
		} catch (ProcessingException e) {
			// FAILED update to FAILED
			
			txnDto.setTxnStatus(TransactionStatusEnum.FAILED.getName());
			txnDto.setErrorCode(e.getErrorCode());
			txnDto.setErrorMessage(e.getErrorMessage());
			paymentStatusService.processStatus(txnDto);
			
			if(e.getErrorCode().equals(Constants.STRIPE_PSP_ERROR)) {
				log.error("Got error with StripePSP, so returning standard error to invoker");
				throw new ProcessingException(
						ErrorCodeEnum.ERROR_AT_STRIPE_PSP.getErrorCode(), 
						ErrorCodeEnum.ERROR_AT_STRIPE_PSP.getErrorMessage(), 
						e.getHttpStatus());
			}
			
			throw e;// re-throwing exception for error response.
		}
	}
	private PaymentResDto processResponse(ResponseEntity<String> httpResponse) {
		if(httpResponse.getStatusCode().isSameCodeAs(HttpStatus.CREATED)) {
			// convert to success PaymentRes object. 
			
			PaymentRes spPaymentRes = gsonUtils.fromJson(httpResponse.getBody(), PaymentRes.class);
			log.info("Converted to PaymentRes:" + spPaymentRes);

			if (spPaymentRes != null && spPaymentRes.getUrl() != null) {
				PaymentResDto paymentResDto = modelMapper.map(spPaymentRes, PaymentResDto.class);
				log.info("Converted PaymentRes to PaymentResDTO:" + paymentResDto);
				return paymentResDto;
			}
			log.error("GOT 201 but no url in response.");
		}
		
		// convert to error object.
		SPErrorResponse errorResponse = gsonUtils.fromJson(httpResponse.getBody(), SPErrorResponse.class);
		log.error("Converted to SPErrorResponse:" + errorResponse);
		

		if (errorResponse != null && errorResponse.getErrorCode() != null) {
			throw new ProcessingException(
					errorResponse.getErrorCode(), 
					errorResponse.getErrorMessage(), 
					HttpStatus.valueOf(httpResponse.getStatusCode().value()));
		}
		
		log.error("Raising Generic error. Unable to get valid error object structure");
		throw new ProcessingException(
				ErrorCodeEnum.GENERIC_ERROR.getErrorCode(), 
				ErrorCodeEnum.GENERIC_ERROR.getErrorMessage(), 
				HttpStatus.INTERNAL_SERVER_ERROR);

	}

	private HttpRequest prepareHttpRequest(InitiatePaymentDto reqDto) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, 
				MediaType.APPLICATION_JSON_VALUE);
log.info("stripeProviderCreatePaymentUrl:" + reqDto);
		CreatePaymentReq paymentReq = modelMapper.map(reqDto, CreatePaymentReq.class);
log.info("stripeProviderCreatePaymentUrl:" + paymentReq);
		//String stripeProviderCreatePaymentUrl = null;
		HttpRequest httpRequest = HttpRequest.builder()
				.method(HttpMethod.POST)
				.url(stripeProviderCreatePaymentUrl)
				.headers(httpHeaders)
				.requestBody(gson.toJson(paymentReq))
				.build();

		log.info("Prepared httpRequest:" + httpRequest);
		return httpRequest;
	}

}
