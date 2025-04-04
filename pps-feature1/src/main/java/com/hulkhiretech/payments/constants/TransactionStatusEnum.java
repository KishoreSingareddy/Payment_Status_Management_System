package com.hulkhiretech.payments.constants;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum TransactionStatusEnum {
	
	CREATED(1, "CREATED"),
	INITIATED(2,"INITIATED"),
	PENDING(3,"PENDING"),
	SUCCESS(4,"SUCCESS"),
	FAILED(5,"FAILED");
	
	private int id;
	private String name;
	
	 TransactionStatusEnum(int id, String name) {
		this.id = id;
		this.name = name;
	}
	 public static TransactionStatusEnum getByName(String name) {
	        for (TransactionStatusEnum status : values()) {
	            if (status.name.equalsIgnoreCase(name)) {
	                return status;
	            }
	        }
	        
	        log.error("No enum constant found for name: " + name);
	        return null;
	    }

	    // Method to get enum by id
	    public static TransactionStatusEnum getById(int id) {
	        for (TransactionStatusEnum status : values()) {
	            if (status.id == id) {
	                return status;
	            }
	        }
	        
	        log.error("No enum constant found for id: " + id);
	        return null;
	    }
	
}
