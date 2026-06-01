package com.zsgs.bankapi.service.managerauthservice;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.zsgs.bankapi.dto.SignupRequest;
import com.zsgs.bankapi.model.Manager;
import com.zsgs.bankapi.repository.ManagerRepository;

@Service
public class AuthService implements IAuthService, UserDetailsService {

	@Autowired
	private ManagerRepository managerRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public String signup(SignupRequest signupRequest) {
		Optional<Manager> existingManager = managerRepository.findByEmail(signupRequest.getEmail());
		if (existingManager.isPresent()) {
			return "Email is already registered!";
		}

		String hashedPassword = passwordEncoder.encode(signupRequest.getPassword());
		Manager manager = new Manager(signupRequest.getName(), hashedPassword, signupRequest.getDob(),
				signupRequest.getEmail(), signupRequest.getMobileNo());

		managerRepository.save(manager);
		return "Registration Successful!";
	}

//	public String signin(LoginRequest loginRequest) {
//		Optional<Manager> managerOptional = managerRepository.findByEmail(loginRequest.getEmail());
//
//		if (managerOptional.isEmpty()) {
//			return "Email not found!";
//		}
//
//		Manager manager = managerOptional.get();
//		if (!passwordEncoder.matches(loginRequest.getPassword(), manager.getPassword())) {
//			return "Incorrect password!";
//		}
//		return "Login Successful!";
//	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Manager managerOptional = managerRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException(username + " This Email not found!"));

		return new User(managerOptional.getEmail(), managerOptional.getPassword(), Collections.singleton(new SimpleGrantedAuthority(managerOptional.getRole())));
	}

}
