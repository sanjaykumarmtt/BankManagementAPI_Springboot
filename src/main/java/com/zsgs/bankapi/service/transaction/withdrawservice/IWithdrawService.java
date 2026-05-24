package com.zsgs.bankapi.service.transaction.withdrawservice;

import java.util.Map;

import com.zsgs.bankapi.dto.TransactionRequest;

public interface IWithdrawService {
	
	public Map<String, Object> withdraw(TransactionRequest request);

}
