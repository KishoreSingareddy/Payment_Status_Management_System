package com.hulkhiretech.payments.util;

import org.modelmapper.AbstractConverter;

import com.hulkhiretech.payments.constants.PaymentMethodEnum;

public class PaymentMethodEnumIdToStringConverter extends AbstractConverter<Integer, String> {
	@Override
		 protected String convert(Integer source) {
		        return PaymentMethodEnum.getById(source).getName();
		    
	}
}
