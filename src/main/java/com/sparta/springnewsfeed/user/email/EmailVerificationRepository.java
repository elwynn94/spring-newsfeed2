package com.sparta.springnewsfeed.user.email;

import com.sparta.springnewsfeed.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findByCode(String code);
    Optional<EmailVerification> findByUser(User user);
}
