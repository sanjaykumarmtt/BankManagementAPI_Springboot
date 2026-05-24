package com.zsgs.bankapi.service.transaction.transderservice;

import java.util.Map;
import java.util.List;

import com.zsgs.bankapi.dto.FundTransferRequest;
import com.zsgs.bankapi.dto.TransactionHistoryResponse;

public interface ITransferService {
	
	public Map<String, Object> fundTransfer(FundTransferRequest request);

	public List<TransactionHistoryResponse> getAllTransactionHistory();
	
	public List<TransactionHistoryResponse> getTransactionHistoryByAccount(String accountNumber);
}
