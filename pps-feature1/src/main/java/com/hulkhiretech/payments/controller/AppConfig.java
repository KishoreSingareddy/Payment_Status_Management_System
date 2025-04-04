package com.hulkhiretech.payments.controller;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hulkhiretech.payments.dto.TransactionDto;
import com.hulkhiretech.payments.entity.TransactionEntity;
import com.hulkhiretech.payments.util.PaymentMethodEnumConverter;
import com.hulkhiretech.payments.util.PaymentMethodEnumIdToStringConverter;
import com.hulkhiretech.payments.util.PaymentTypeEnumConverter;
import com.hulkhiretech.payments.util.PaymentTypeEnumIdToStringConverter;
import com.hulkhiretech.payments.util.ProviderEnumConverter;
import com.hulkhiretech.payments.util.ProviderEnumIdToStringConverter;
import com.hulkhiretech.payments.util.TransactionStatusEnumConverter;
import com.hulkhiretech.payments.util.TransactionStatusEnumIdToStringConverter;

@Configuration
public class AppConfig {
	
	@Bean
	 ModelMapper createObjectModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		Converter<String, Integer> paymentMethodEnumConverter = new PaymentMethodEnumConverter();
		Converter<String, Integer> paymentTypeEnumConverter = new PaymentTypeEnumConverter();
		Converter<String, Integer> providerEnumConverter = new ProviderEnumConverter();
		Converter<String, Integer> transactionStatusEnumConverter = new TransactionStatusEnumConverter();
		
		modelMapper.addMappings(new PropertyMap<TransactionDto, TransactionEntity>(){
			@Override
			protected void configure() {
				// TODO Auto-generated method stub
				using(paymentMethodEnumConverter).map(source.getPaymentMethod(), destination.getPaymentMethodId());
				using(transactionStatusEnumConverter).map(source.getTxnStatus(), destination.getTxnStatusId());
				using(providerEnumConverter).map(source.getProvider(), destination.getProviderId());
				using(paymentTypeEnumConverter).map(source.getPaymentType(),destination.getPaymentTypeId());
			}
		});		
		
		
		Converter<Integer, String> paymentMethodEnumIdToStringConverter = new PaymentMethodEnumIdToStringConverter();
		Converter<Integer, String> paymentTypeEnumIdToStringConverter = new PaymentTypeEnumIdToStringConverter();
		Converter<Integer, String> providerEnumIdToStringConverter = new ProviderEnumIdToStringConverter();
		Converter<Integer, String> transactionStatusEnumIdToStringConverter = new TransactionStatusEnumIdToStringConverter();
		
		
		
		modelMapper.addMappings(new PropertyMap<TransactionEntity, TransactionDto>(){
			@Override
			protected void configure() {
				// TODO Auto-generated method stub
				using(paymentMethodEnumIdToStringConverter).map(source.getPaymentMethodId(), destination.getPaymentMethod());
				using(transactionStatusEnumIdToStringConverter).map(source.getTxnStatusId(), destination.getTxnStatus());
				using(providerEnumIdToStringConverter).map(source.getProviderId(), destination.getProvider());
				using(paymentTypeEnumIdToStringConverter).map(source.getPaymentTypeId(),destination.getPaymentType());
			}
		});	
		return modelMapper;
	}
}
