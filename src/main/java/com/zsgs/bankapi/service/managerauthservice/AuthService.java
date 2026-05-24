package com.zsgs.bankapi.service.managerauthservice;

import com.zsgs.bankapi.dto.LoginRequest;
import com.zsgs.bankapi.dto.SignupRequest;
import com.zsgs.bankapi.model.Manager;
import com.zsgs.bankapi.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService implements IAuthService  {

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
        Manager manager = new Manager(
                signupRequest.getName(),
                hashedPassword,
                signupRequest.getDob(),
                signupRequest.getEmail(),
                signupRequest.getMobileNo()
        );

        managerRepository.save(manager);
        return "Registration Successful!";
    }

    public String signin(LoginRequest loginRequest) {
        Optional<Manager> managerOptional = managerRepository.findByEmail(loginRequest.getEmail());

        if (managerOptional.isEmpty()) {
            return "Email not found!";
        }

        Manager manager = managerOptional.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), manager.getPassword())) {
            return "Incorrect password!";
        }
        return "Login Successful!";
    }
}
