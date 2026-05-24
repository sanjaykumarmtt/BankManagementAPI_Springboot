package com.zsgs.bankapi.service.accountservice;

import java.util.List;
import java.util.Map;

import com.zsgs.bankapi.dto.AccountCreationRequest;

public interface IAccountService {
	
	public Long createAccount(AccountCreationRequest request);
	
	public AccountCreationRequest getAccountReview(String accountNumber);
	
	public List<AccountCreationRequest> getAllAccountReview();
}
