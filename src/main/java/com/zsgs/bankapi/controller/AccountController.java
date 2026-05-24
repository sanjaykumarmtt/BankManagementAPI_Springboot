package com.zsgs.bankapi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zsgs.bankapi.dto.AccountCreationRequest;
import com.zsgs.bankapi.dto.FundTransferRequest;
import com.zsgs.bankapi.dto.TransactionHistoryResponse;
import com.zsgs.bankapi.dto.TransactionRequest;
import com.zsgs.bankapi.service.accountservice.IAccountService;
import com.zsgs.bankapi.service.transaction.depositservice.IDepositService;
import com.zsgs.bankapi.service.transaction.transderservice.ITransferService;
import com.zsgs.bankapi.service.transaction.withdrawservice.IWithdrawService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/accounts")
public class AccountController {

	@Autowired
	private IAccountService iAccountService;

	@Autowired
	private IDepositService iDepositService;

	@Autowired
	private IWithdrawService iWithdrawService;

	@Autowired
	private ITransferService iTransferService;

	@PostMapping("/create")
	public ResponseEntity<Map<String, Object>> createAccount(@Valid @RequestBody AccountCreationRequest request) {
		try {
			Long accountNumber = iAccountService.createAccount(request);

			Map<String, Object> response = new HashMap<>();
			response.put("message", "Account creation successfully finished!");
			response.put("accountNumber", accountNumber);

			return new ResponseEntity<>(response, HttpStatus.CREATED);
			
		} catch (IllegalArgumentException e) {
			System.out.println("hello Eoor");
			Map<String, Object> response = new HashMap<>();
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", "An internal error occurred while creating the account.");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
	}

	@GetMapping("/review/{accountNumber}")
	public ResponseEntity<?> getAccountReview(@PathVariable String accountNumber) {
		try {
			AccountCreationRequest review = iAccountService.getAccountReview(accountNumber);
			return ResponseEntity.ok(review);
		} catch (IllegalArgumentException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", "An internal error occurred while fetching the account review.");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/review")
	public ResponseEntity<?> getAllAccountReview() {
		try {
			List<AccountCreationRequest> review = iAccountService.getAllAccountReview();
			return ResponseEntity.ok(review);
		} catch (IllegalArgumentException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", "An internal error occurred while fetching the account review.");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/deposit")
	public ResponseEntity<Map<String, Object>> deposit(@Valid @RequestBody TransactionRequest request) {
		try {
			Map<String, Object> response = iDepositService.deposit(request);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", "An internal error occurred while processing the deposit.");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/withdraw")
	public ResponseEntity<Map<String, Object>> withdraw(@Valid @RequestBody TransactionRequest request) {
		try {
			Map<String, Object> response = iWithdrawService.withdraw(request);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", "An internal error occurred while processing the withdrawal.");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/transfer")
	public ResponseEntity<Map<String, Object>> fundTransfer(@Valid @RequestBody FundTransferRequest request) {
		try {
			Map<String, Object> response = iTransferService.fundTransfer(request);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", "An internal error occurred while processing the fund transfer.");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/transactions/all")
	public ResponseEntity<?> getAllTransactionHistory() {
		try {
			List<TransactionHistoryResponse> history = iTransferService.getAllTransactionHistory();
			return ResponseEntity.ok(history);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", "An internal error occurred while fetching transaction history.");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/transactions/review/{accountNumber}")
	public ResponseEntity<?> getTransactionHistoryByAccount(@PathVariable String accountNumber) {
		try {
			List<TransactionHistoryResponse> history = iTransferService.getTransactionHistoryByAccount(accountNumber);
			return ResponseEntity.ok(history);
		} catch (IllegalArgumentException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", "An internal error occurred while fetching transaction history.");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}