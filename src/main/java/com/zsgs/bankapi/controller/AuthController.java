package com.zsgs.bankapi.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zsgs.bankapi.config.cookis.CookieMaintenance;
import com.zsgs.bankapi.config.security.JwtUtilPak;
import com.zsgs.bankapi.dto.LoginRequest;
import com.zsgs.bankapi.dto.SignupRequest;
import com.zsgs.bankapi.service.managerauthservice.IAuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private IAuthService iAuthService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	JwtUtilPak jwtUtilPak;
	
	@Autowired
	CookieMaintenance cookieMaintenance;

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest signupRequest) {
		String result = iAuthService.signup(signupRequest);
		if (result.equals("Registration Successful!")) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);
	}

	@PostMapping("/signin")
	public ResponseEntity<Map<String, String>> signin(@RequestBody LoginRequest loginRequest,HttpServletResponse response) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();

			String token = jwtUtilPak.generateTolen(userDetails);
			cookieMaintenance.setJwtCookie(response, token);
			return ResponseEntity.ok(Map.of("Message", "Logged in successfully", "token", token));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", "Invalid User Name OR password"));
		}
	}
	
	@PostMapping("/signout")
	public ResponseEntity<String> signOut(HttpServletResponse response) {
	 
	    cookieMaintenance.clearJwtCookie(response);
	    return ResponseEntity.ok("Logged out successfully");
	}
	@GetMapping("/validate")
	public ResponseEntity<Map<String, String>> validateSession(jakarta.servlet.http.HttpServletRequest request) {
		String token = null;
		

		// 1. Extract the JWT from the cookies
		if (request.getCookies() != null) {
			for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
				if ("jwt".equals(cookie.getName())) {
					token = cookie.getValue();
					break;
				}
			}
		}

		if (token == null) {
			String authHeader = request.getHeader("Authorization");
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				token = authHeader.substring(7);
			}
		}

		// 2. Return 401 if token is missing
		if (token == null) {
			return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
		}

		try {
			// 3. Validate token expiration
			if (jwtUtilPak.isTokenExpired(token)) {
				return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
			}

			// 4. Extract the role from the claims 
			// (Note: using "rolrs" as that is how it is spelled in your JwtUtilPak.generateTolen method)
			Object rolesClaim = jwtUtilPak.extractClaim(token, claims -> claims.get("rolrs"));
			
			String role = "USER"; // Default
			if (rolesClaim != null) {
				String rolesStr = rolesClaim.toString();
				
				System.out.println(rolesStr);
				// Check for MANAGER or ADMIN authorities
				if (rolesStr.contains("MANAGER")) {
					role = "MANAGER";
				}
			}

			Map<String, String> responseBody = new HashMap<>();
			responseBody.put("role", role);

			Object accNoClaim = jwtUtilPak.extractClaim(token, claims -> claims.get("accNo"));
			if (accNoClaim != null) {
				responseBody.put("accountNumber", accNoClaim.toString());
			}

			// 5. Return success JSON with role and optionally accountNumber
			return ResponseEntity.ok(responseBody);
			
		} catch (Exception e) {
			// Catch parsing errors, invalid signatures, etc., and return 401
			return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
		}
	}

	

}