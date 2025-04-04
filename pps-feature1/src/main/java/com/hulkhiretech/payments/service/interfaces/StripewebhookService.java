package com.hulkhiretech.payments.service.interfaces;

import com.hulkhiretech.payments.dto.StripeEventDto;

public interface StripewebhookService {

	public void processStripeEvent(StripeEventDto stripeEventDto);
}
