package com.hulkhiretech.payments.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hulkhiretech.payments.dto.InitiatePaymentDto;
import com.hulkhiretech.payments.dto.TransactionDto;
import com.hulkhiretech.payments.pojo.CreatePaymentRequest;
import com.hulkhiretech.payments.pojo.CreatePaymentResponse;
import com.hulkhiretech.payments.pojo.InitPaymnetResponse;
import com.hulkhiretech.payments.pojo.InitiatePaymentReq;
import com.hulkhiretech.payments.service.impl.PaymentServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/payments")
@Slf4j
public class PaymentController {
	
	private PaymentServiceImpl paymentService;
	private ModelMapper modelMapper;
	
	public PaymentController(ModelMapper modelMapper, PaymentServiceImpl paymentService) {
		super();
		this.modelMapper = modelMapper;
		this.paymentService = paymentService;
	}

	@PostMapping
	public ResponseEntity<CreatePaymentResponse> createPayment(
			@RequestBody CreatePaymentRequest createPaymentRequest) {
	
		log.info("Payment created successfully: "
				+ "createPaymentRequest:" + createPaymentRequest);
		
		TransactionDto txnDto = modelMapper.map(createPaymentRequest, TransactionDto.class);
		log.info("TransactionDTO created successfully: txnDto:" + txnDto);
		
		TransactionDto response = paymentService.createPayment(txnDto);
		
		CreatePaymentResponse createPaymentResponse = new CreatePaymentResponse();
		createPaymentResponse.setTxnReference(response.getTxnReference());
		createPaymentResponse.setTxnStatus(response.getTxnStatus());
		
		log.info("Retunring createPaymentResponse:{}", createPaymentResponse);
		
		return new ResponseEntity<>(
				createPaymentResponse, HttpStatus.CREATED); 
	}
	@PostMapping("/{txnReference}/initiate")
	
	public ResponseEntity<InitPaymnetResponse> initiatePayment(
			@PathVariable String txnReference, 
			@RequestBody InitiatePaymentReq initiatePaymentReq) {
		log.info("Payment status fetched successfully||txnReference:{}|initiatePaymentReq:{}", 
				txnReference, initiatePaymentReq);
		log.trace("Payment created successfully: "
				+ "createPaymentRequest:" + txnReference);
		log.debug("Payment created successfully: "
				+ "createPaymentRequest:" + txnReference);
		log.warn("Payment created successfully: "
				+ "createPaymentRequest:" + txnReference);
		log.error("Payment created successfully: "	
				+ "createPaymentRequest:" + txnReference);
		
		InitiatePaymentDto reqDto = modelMapper.map(initiatePaymentReq, InitiatePaymentDto.class);
		
		TransactionDto responseDto = paymentService.initiatePayment(txnReference, reqDto);
		
		InitPaymnetResponse response = InitPaymnetResponse.builder()
				.txnReference(responseDto.getTxnReference())
				.txnStatus(responseDto.getTxnStatus())
				.url(responseDto.getUrl())
				.build();
		log.info("Retunring response:{}", response);
		
		return ResponseEntity.ok(response);
	}
}