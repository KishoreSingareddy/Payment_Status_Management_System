package com.hulkhiretech.payments.util;

import org.modelmapper.AbstractConverter;

import com.hulkhiretech.payments.constants.ProviderEnum;

public class ProviderEnumIdToStringConverter extends AbstractConverter<Integer, String> {
	@Override
	 protected String convert(Integer source) {
        return ProviderEnum.getById(source).getName();
    
	}
}
