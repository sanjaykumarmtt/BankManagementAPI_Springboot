package com.zsgs.bankapi.dto;

import jakarta.validation.constraints.*;

public class AccountCreationRequest {
	
	private Integer userId;
	
	private long accountNumber;
	
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 4, max = 50, message = "Name must be between 4 and 50 characters")
    private String name;

    @NotBlank(message = "Mobile Number cannot be empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile Number must be exactly 10 digits")
    private String mobileNo;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    @NotBlank(message = "Account Type cannot be empty")
    @Pattern(regexp = "^(Savings|Current)$", message = "Account Type must be Savings or Current")
    private String accountType;

    @Min(value = 500, message = "Minimum initial balance must be at least 500")
    private Double initialBalance = 500.0;
    
    private String role="USER";

    public AccountCreationRequest() {}

    public AccountCreationRequest(Integer userId,long accountNumber, String name, String mobileNo, String email, String address, String accountType, Double initialBalance) {
        this.userId = userId;
        this.accountNumber=accountNumber;
        this.name = name;
        this.mobileNo = mobileNo;
        this.email = email;
        this.address = address;
        this.accountType = accountType;
        this.initialBalance = initialBalance;
    }
    
    
    
	
	public String getRole() {
		return role;
	}

	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Double getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(Double initialBalance) {
        this.initialBalance = initialBalance;
    }
}
