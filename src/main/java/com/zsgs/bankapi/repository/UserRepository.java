package com.zsgs.bankapi.repository;

import com.zsgs.bankapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
}
