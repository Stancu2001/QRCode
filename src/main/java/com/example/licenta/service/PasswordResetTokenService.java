package com.example.licenta.service;

import com.example.licenta.entities.PasswordResetToken;
import com.example.licenta.entities.User;

import java.time.LocalDateTime;

public interface PasswordResetTokenService {
    boolean sendEmail(User user);

    PasswordResetToken generateResetToken(User user);

    boolean hasExpired(LocalDateTime expiryDateTime);

    boolean canGenerateNewResetToken(User user);

    void saveToken(PasswordResetToken passwordResetToken);

    PasswordResetToken findByToken(String token);

    boolean hasValidResetToken(PasswordResetToken passwordResetToken);

    boolean isTokenUsedOrDateExpired(PasswordResetToken passwordResetToken);
}
