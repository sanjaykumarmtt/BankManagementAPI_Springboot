package com.zsgs.bankapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zsgs.bankapi.dto.LoginRequest;
import com.zsgs.bankapi.dto.SignupRequest;
import com.zsgs.bankapi.service.managerauthservice.IAuthService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private IAuthService iAuthService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest signupRequest) {
        String result = iAuthService.signup(signupRequest);
        if (result.equals("Registration Successful!")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody LoginRequest loginRequest) {
    	System.out.println(loginRequest.getPassword());
        String result = iAuthService.signin(loginRequest);
        if (result.equals("Login Successful!")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().body(result);
    }
}