package com.hulkhiretech.payments.service.impl;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.hulkhiretech.payments.constants.TransactionStatusEnum;
import com.hulkhiretech.payments.dao.interfaces.TransactionDao;
import com.hulkhiretech.payments.dto.CheckoutSessionCompletedData;
import com.hulkhiretech.payments.dto.StripeEventDto;
import com.hulkhiretech.payments.dto.TransactionDto;
import com.hulkhiretech.payments.service.interfaces.PaymentStatusService;
import com.hulkhiretech.payments.service.interfaces.StripewebhookService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class StripeWebhookServiceImpl implements StripewebhookService {

	private static final String PAYMENT_STATUS_UNPAID = "unpaid";
	private static final String PAYMENT_STATUS_PAID = "paid";
	private static final String COMPLETE = "complete";
	private static final String CHECKOUT_SESSION_ASYNC_PAYMENT_FAILED = "checkout.session.async_payment_failed";
	private static final String CHECKOUT_SESSION_ASYNC_PAYMENT_SUCCEEDED = "checkout.session.async_payment_succeeded";
	private static final String CHECKOUT_SESSION_COMPLETED = "checkout.session.completed";

	private final PaymentStatusService paymentStatusService;
	private final Gson gson;
	private final TransactionDao transactionDao;

	@Override
	public void processStripeEvent(StripeEventDto eventDto) {
		if(CHECKOUT_SESSION_COMPLETED.equals(eventDto.getType())) {

			CheckoutSessionCompletedData objData =gson.fromJson(eventDto.getData().getObject(), CheckoutSessionCompletedData.class);
			log.info("Checkout session completed with payment status: {}",objData);

			if( COMPLETE.equals(objData.getStatus()) && PAYMENT_STATUS_PAID.equals(objData.getPaymentStatus())) {
				log.info("Payment status is paid");

				TransactionDto txnDto=transactionDao.getTransactionByProviderReference(objData.getId());
				if(txnDto==null) {
					log.error("Transaction not found for reference: {}",objData.getId());
					return;
				}
				
				log.info("Transaction found: {}",txnDto);
				txnDto.setTxnStatus(TransactionStatusEnum.SUCCESS.getName());
				paymentStatusService.processStatus(txnDto);
			}
			return;
		}

		if(CHECKOUT_SESSION_ASYNC_PAYMENT_SUCCEEDED.equals(eventDto.getType())) {
			log.info("Checkout session async payment succeeded");
			
			return;
		}

		if(CHECKOUT_SESSION_ASYNC_PAYMENT_FAILED.equals(eventDto.getType())) {
			
			CheckoutSessionCompletedData objData =gson.fromJson(eventDto.getData().getObject(), CheckoutSessionCompletedData.class);
            log.info("Checkout session async payment failed with payment status: {}", objData);
			if( PAYMENT_STATUS_UNPAID.equals(objData.getPaymentStatus())) {
				log.info("Payment status is unpaid");
			TransactionDto txnDto=transactionDao.getTransactionByProviderReference(objData.getId());
			if(txnDto==null) {
				log.error("Transaction not found for reference: {}",objData.getId());
				return;
			}
			log.info("Transaction not found: {}",txnDto);
			txnDto.setTxnStatus(TransactionStatusEnum.FAILED.getName());
			paymentStatusService.processStatus(txnDto);
			
			log.info("Checkout session async payment failed");
			return;
		}
		log.info("Event type not found",eventDto.getType());
	}

}
}
