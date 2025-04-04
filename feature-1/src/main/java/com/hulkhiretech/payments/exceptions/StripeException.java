package com.hulkhiretech.payments.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class  StripeException extends RuntimeException{

	    private final String errorCode;
	    private final String errorMessage;
	    private final HttpStatus httpStatus;

	    public StripeException(String errorCode, String errorMessage,HttpStatus httpStatus) {
	        super(errorMessage);
	        this.errorCode = errorCode;
	        this.errorMessage = errorMessage;
			this.httpStatus = httpStatus;

	    }

}
