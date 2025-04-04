package com.hulkhiretech.payments.util;

import org.modelmapper.AbstractConverter;

import com.hulkhiretech.payments.constants.PaymentTypeEnum;

public class PaymentTypeEnumIdToStringConverter extends AbstractConverter<Integer, String> {
	@Override
	 protected String convert(Integer source) {
        return PaymentTypeEnum.getById(source).getName();
    
	}
}
