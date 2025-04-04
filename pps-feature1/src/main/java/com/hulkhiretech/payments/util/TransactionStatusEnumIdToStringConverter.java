package com.hulkhiretech.payments.util;

import org.modelmapper.AbstractConverter;

import com.hulkhiretech.payments.constants.TransactionStatusEnum;

public class TransactionStatusEnumIdToStringConverter extends AbstractConverter<Integer, String> {
	@Override
	protected String convert(Integer source) {
		return TransactionStatusEnum.getById(source).getName();
	    
	}
}
