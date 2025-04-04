package com.hulkhiretech.payments.util;

import org.modelmapper.AbstractConverter;

import com.hulkhiretech.payments.constants.ProviderEnum;

public class ProviderEnumConverter extends AbstractConverter<String, Integer> {
	 @Override
	    protected Integer convert(String source) {
	        return ProviderEnum.getByName(source).getId();
	    }
}
