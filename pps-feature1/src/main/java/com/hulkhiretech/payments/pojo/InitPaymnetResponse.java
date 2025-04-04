package com.hulkhiretech.payments.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InitPaymnetResponse {
	private String txnReference;
	private String txnStatus;
	private String url;
}
