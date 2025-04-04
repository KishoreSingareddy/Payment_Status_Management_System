package com.hulkhiretech.payments.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import com.hulkhiretech.payments.constants.ErrorCodeEnum;
import com.hulkhiretech.payments.exceptions.StripeException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HttpServiceEngine {
	
	
	private RestClient restClient;
	
	public HttpServiceEngine(RestClient.Builder restClientBuilder) {
		this.restClient = restClientBuilder.build();
	}
	
	public ResponseEntity<String> makeHttpCall(HttpRequest httpRequest) {
		log.info("invoked makeHttpCall|httpRequest:" + httpRequest);
		
		try {
			ResponseEntity<String> response = restClient.method(httpRequest.getMethod())
					.uri(httpRequest.getUrl())
					
					.headers(headers -> headers.addAll(httpRequest.getHeaders()))
					
					.body(httpRequest.getRequestBody())
					
					.retrieve()
					
					.toEntity(String.class);
			
			log.info("Response:" + response);
			return response;
		} catch(HttpClientErrorException | HttpServerErrorException e) {
			// all possible error responses will come here.
            log.error("HttpClientErrorException occurred:", e.getResponseBodyAsString());
            
            //if HttpServerErrorException is Gateway timeout or service unavailable 
            //that is also network failure or not response.
            if(e.getStatusCode().equals(HttpStatus.GATEWAY_TIMEOUT) 
            		|| e.getStatusCode().equals(HttpStatus.SERVICE_UNAVAILABLE)) {
            	log.error("received error from 5xx statusCode:" + e.getStatusCode(), e);
            	
            	throw new StripeException(
    					ErrorCodeEnum.UNABLE_TO_CONNECT_TO_STRIPE_PSP.getErrorCode(),
    					ErrorCodeEnum.UNABLE_TO_CONNECT_TO_STRIPE_PSP.getErrorMessage(),
    					HttpStatus.valueOf(e.getStatusCode().value()));
            }
            
            // valid error response handling.
            
            log.info("Got getResponseBodyAsString:" + e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode())
            		.body(e.getResponseBodyAsString());
            
        } catch (Exception e) {// unable to get response.
			log.error("Exception occurred:", e);
			throw new StripeException(
					ErrorCodeEnum.UNABLE_TO_CONNECT_TO_STRIPE_PSP.getErrorCode(), 
					ErrorCodeEnum.UNABLE_TO_CONNECT_TO_STRIPE_PSP.getErrorMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
			
			// 500 INTERNAL_SERVER_ERROR
		}
	}

}