package com.zsgs.bankapi.service.managerauthservice;

import com.zsgs.bankapi.dto.SignupRequest;

public interface IAuthService {
	
	public String signup(SignupRequest signupRequest);
	
//	public String signin(LoginRequest loginRequest);

}
