package com.hulkhiretech.payments.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.hulkhiretech.payments.dto.StripeEventDto;
import com.hulkhiretech.payments.pojo.StripeEvent;
import com.hulkhiretech.payments.service.interfaces.StripewebhookService;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/stripe/webhook")
@Slf4j
@RequiredArgsConstructor
public class StripeWebhookController {

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;
    
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final StripewebhookService stripeWebhookService;
    
    @PostMapping
    public ResponseEntity<String> processStripeEvent(@RequestBody String eventBody, 
                    @RequestHeader("Stripe-Signature") String signatureHeader) {
        log.info("Stripe-Signature: {}", signatureHeader);

        try {
            Event event = Webhook.constructEvent(eventBody, signatureHeader, endpointSecret);
            log.info("Webhook verified successfully: {}", event.getType());
        } catch (Exception e) {
            log.error("Invalid signature: {}");
           return ResponseEntity.badRequest().build();
        }
        StripeEvent event=gson.fromJson(eventBody, StripeEvent.class);
		log.info("Event: {}", event.getType());
		StripeEventDto eventDto=modelMapper.map(event, StripeEventDto.class);
		stripeWebhookService.processStripeEvent(eventDto);
		log.info("Event processed successfully");
            return ResponseEntity.ok().build();
        
    }
}
