package com.hulkhiretech.payments.constants;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum PaymentTypeEnum {
	SALE(1, "SALE");
	
	private int id;
	private String name;
	
	PaymentTypeEnum(int id, String name) {
		this.id = id;
		this.name = name;
	}
	 public static PaymentTypeEnum getByName(String name) {
	        for (PaymentTypeEnum status : values()) {
	            if (status.name.equalsIgnoreCase(name)) {
	                return status;
	            }
	        }
	        
	        log.error("No enum constant found for name: " + name);
	        return null;
	    }

	    // Method to get enum by id
	    public static PaymentTypeEnum getById(int id) {
	        for (PaymentTypeEnum status : values()) {
	            if (status.id == id) {
	                return status;
	            }
	        }
	        
	        log.error("No enum constant found for id: " + id);
	        return null;
	    }

}
