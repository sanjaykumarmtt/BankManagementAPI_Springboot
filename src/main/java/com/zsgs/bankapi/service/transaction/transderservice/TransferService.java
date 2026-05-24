package com.zsgs.bankapi.service.transaction.transderservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zsgs.bankapi.dto.FundTransferRequest;
import com.zsgs.bankapi.dto.TransactionHistoryResponse;
import com.zsgs.bankapi.model.Account;
import com.zsgs.bankapi.service.transaction.BaseTransactionService;

@Service
public class TransferService extends BaseTransactionService implements ITransferService{

	@Transactional
	public Map<String, Object> fundTransfer(FundTransferRequest request) {
		if (request.getSenderAccountNumber().equals(request.getReceiverAccountNumber())) {
			throw new IllegalArgumentException("Sender and Receiver accounts cannot be the same.");
		}

		Account sender = getValidatedAccount(request.getSenderAccountNumber());
		Account receiver = getValidatedAccount(request.getReceiverAccountNumber());

		if (!isValidateSufficientBalance(sender, request.getAmount()))
			throw new IllegalArgumentException("Insufficient balance in account: " + sender.getAccountNumber());

		sender.setBalance(sender.getBalance() - request.getAmount());
		accountRepository.save(sender);

		receiver.setBalance(receiver.getBalance() + request.getAmount());
		accountRepository.save(receiver);

		saveTransactionHistory(sender, -request.getAmount(), "FundTransfer", request.getReceiverAccountNumber(),
				receiver.getUser().getUserName());

		saveTransactionHistory(receiver, request.getAmount(), "Fund Deposit", request.getSenderAccountNumber(),
				sender.getUser().getUserName());

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Fund transfer successful!");
		response.put("SenderName", sender.getUser().getUserName());
		response.put("ReceiverName", receiver.getUser().getUserName());
		response.put("SenderAccountNumber", sender.getAccountNumber());
		response.put("SendernewBalance", sender.getBalance());

		return response;
	}

	@Override
	public List<TransactionHistoryResponse> getAllTransactionHistory() {
		return transactionRepository.findAllTransactionHistory();
	}

	@Override
	public List<TransactionHistoryResponse> getTransactionHistoryByAccount(String accountNumber) {
		Long accountNum;
		try {
			accountNum = Long.parseLong(accountNumber);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid account number format.");
		}
		return transactionRepository.findTransactionHistoryByAccountNumber(accountNum);
	}
}
