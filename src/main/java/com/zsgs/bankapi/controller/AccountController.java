package com.zsgs.bankapi.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zsgs.bankapi.config.cookis.CookieMaintenance;
import com.zsgs.bankapi.config.security.JwtUtilPak;
import com.zsgs.bankapi.dto.AccountCreationRequest;
import com.zsgs.bankapi.dto.FundTransferRequest;
import com.zsgs.bankapi.dto.TransactionHistoryResponse;
import com.zsgs.bankapi.dto.TransactionRequest;
import com.zsgs.bankapi.service.accountservice.IAccountService;
import com.zsgs.bankapi.service.transaction.depositservice.IDepositService;
import com.zsgs.bankapi.service.transaction.transderservice.ITransferService;
import com.zsgs.bankapi.service.transaction.withdrawservice.IWithdrawService;

import jakarta.servlet.http.HttpServletResponse;
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
	
	@Autowired
	private CookieMaintenance cookieMaintenance;
	
	@Autowired
	JwtUtilPak jwtUtilPak;

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
	
	
	@GetMapping("/review/manager/{accountNumber}")
	public ResponseEntity<?> getAccountReviewManagerOnley(@PathVariable String accountNumber,HttpServletResponse respons) {
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


	@GetMapping("/review/{accountNumber}")
	public ResponseEntity<?> getAccountReview(@PathVariable String accountNumber,HttpServletResponse respons) {
		try {
			AccountCreationRequest review = iAccountService.getAccountReview(accountNumber);
			
			UserDetails userDetails = new User(
				    review.getEmail(), 
				    "{noop}password", 
				    Collections.singleton(new SimpleGrantedAuthority(review.getRole())) 
				);

			String token = jwtUtilPak.generateTolenUser(userDetails,accountNumber);
			
			System.out.println(token);
			cookieMaintenance.setJwtCookie(respons, token);
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

	@PostMapping("/user/deposit")
	public ResponseEntity<Map<String, Object>> deposit(@Valid @RequestBody TransactionRequest request, jakarta.servlet.http.HttpServletRequest httpServletRequest) {
		try {
			String accountNumber = getAccountNumberFromToken(httpServletRequest);
			if (accountNumber == null) {
			    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token context"));
			}
			request.setAccountNumber(accountNumber);
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

	@PostMapping("/user/withdraw")
	public ResponseEntity<Map<String, Object>> withdraw(@Valid @RequestBody TransactionRequest request, jakarta.servlet.http.HttpServletRequest httpServletRequest) {
		try {
			String accountNumber = getAccountNumberFromToken(httpServletRequest);
			if (accountNumber == null) {
			    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token context"));
			}
			request.setAccountNumber(accountNumber);
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

	@PostMapping("/user/transfer")
	public ResponseEntity<Map<String, Object>> fundTransfer(@Valid @RequestBody FundTransferRequest request, jakarta.servlet.http.HttpServletRequest httpServletRequest) {
		try {
			String accountNumber = getAccountNumberFromToken(httpServletRequest);
			if (accountNumber == null) {
			    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token context"));
			}
			request.setSenderAccountNumber(accountNumber);
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

	@GetMapping("/user/transactions/review")
	public ResponseEntity<?> getTransactionHistoryByAccount(jakarta.servlet.http.HttpServletRequest request) {
		try {
			String accountNumber = getAccountNumberFromToken(request);
			if (accountNumber == null) {
			    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token context"));
			}

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

	@GetMapping("/manager/search/{accountNumber}")
	public ResponseEntity<?> getTransactionHistoryByAccountManager(@PathVariable String accountNumber) {
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

	@GetMapping("/user/me")
	public ResponseEntity<?> getCurrentUserProfile(jakarta.servlet.http.HttpServletRequest request) {
		try {
			String accountNumber = getAccountNumberFromToken(request);
			if (accountNumber == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token context"));
			}
			AccountCreationRequest review = iAccountService.getAccountReview(accountNumber);
			return ResponseEntity.ok(review);
		} catch (IllegalArgumentException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", "An internal error occurred while fetching the user profile.");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private String getAccountNumberFromToken(jakarta.servlet.http.HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			return jwtUtilPak.extractClaim(token, claims -> claims.get("accNo", String.class));
		}
		
		if (request.getCookies() != null) {
			for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
				if ("jwt".equals(cookie.getName())) {
					String token = cookie.getValue();
					return jwtUtilPak.extractClaim(token, claims -> claims.get("accNo", String.class));
				}
			}
		}
		return null;
	}
}