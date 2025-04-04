package com.hulkhiretech.payments.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hulkhiretech.payments.dto.CreatePaymentDto;
import com.hulkhiretech.payments.dto.PaymentDto;
import com.hulkhiretech.payments.pojo.CreatePaymentReq;
import com.hulkhiretech.payments.pojo.PaymentRes;
import com.hulkhiretech.payments.service.interfaces.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/payments")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {
	
	private final PaymentService paymentService;
	private final ModelMapper modelMapper;
	
	@PostMapping
	public ResponseEntity<PaymentRes>createpayment(@RequestBody CreatePaymentReq createPaymentReq) {
		log.info("payment created successfully"+ createPaymentReq);
		//convert of obj
		
		CreatePaymentDto paymentDto = modelMapper.map(createPaymentReq, CreatePaymentDto.class);
		
		log.info(" Converted to obj  mapper "+ paymentDto);
		//calling the for req
		
		 PaymentDto response  = paymentService.createPayment(paymentDto);
		//handling the resposne 
		 
		 PaymentRes paymentRes = modelMapper.map(response, PaymentRes.class);
		log.info("Created to payemntdto :"+paymentRes);
		return new ResponseEntity<>(paymentRes, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PaymentRes> getpayment(@PathVariable String id) {
		log.info("payment fetched successfully");
     PaymentDto response = paymentService.getPayment(id);

     
      //  return ResponseEntity.ok(paymentDto);
     PaymentRes paymentRes = modelMapper.map(response, PaymentRes.class);
		log.info("Created to payemntdto :"+paymentRes);
		return new ResponseEntity<>(paymentRes, HttpStatus.OK);

	}
	
	@PostMapping("/{id}/expire")
	public ResponseEntity<PaymentRes> expirepayment(@PathVariable String id) {
		log.info("payment expired called"+ id);
		
		PaymentDto response = paymentService.expirePayment(id);
		PaymentRes paymentRes = modelMapper.map(response, PaymentRes.class);
		log.info("payment link expired :"+paymentRes);
		return new ResponseEntity<>(paymentRes, HttpStatus.OK);
	}
}
