package com.zsgs.bankapi.service.transaction.depositservice;

import java.util.Map;

import com.zsgs.bankapi.dto.TransactionRequest;

public interface IDepositService {
	
	public Map<String, Object> deposit(TransactionRequest request);

}
