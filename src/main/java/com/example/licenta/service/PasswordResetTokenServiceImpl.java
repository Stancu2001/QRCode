package com.example.licenta.service;

import com.example.licenta.entities.PasswordResetToken;
import com.example.licenta.entities.User;
import com.example.licenta.repositories.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JavaMailSender javaMailSender;
//    @Value("${travel-journal.appBaseUrl}")
//    private String appBaseUrl;
    @Value("${spring.mail.username}")
    private String senderEmail;
    public PasswordResetTokenServiceImpl(PasswordResetTokenRepository passwordResetTokenRepository, JavaMailSender javaMailSender) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void saveToken(PasswordResetToken passwordResetToken){
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public PasswordResetToken findByToken(String token){
        return passwordResetTokenRepository.findByToken(token);
    }

    @Override
    public boolean sendEmail(User user) {
        try {
            PasswordResetToken resetToken=generateResetToken(user);
            String resetLink = "http://localhost:8080/";

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(senderEmail);
            msg.setTo(user.getEmail());

            msg.setSubject("Reset Password");
            LocalDateTime dateTime = LocalDateTime.now().plusMinutes(30);
            String formattedTime = dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            msg.setText("Hello " + user.getFirstName() + " " + user.getLastName() + "\n\n" + "Please click on this link to reset your Password: " + resetLink + resetToken.getToken() + " . \n\n" + "This link will automatically expire on the hour: " + formattedTime);

            javaMailSender.send(msg);
            passwordResetTokenRepository.save(resetToken);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public PasswordResetToken generateResetToken(User user) {
        UUID uuid = UUID.randomUUID();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(uuid.toString());
        resetToken.setExpireDate(LocalDateTime.now().plusMinutes(30));
        resetToken.setUser(user);
        resetToken.setUsed(false);
        return resetToken;
    }

    @Override
    public boolean hasExpired(LocalDateTime expiryDateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return expiryDateTime.isAfter(currentDateTime);
    }

    @Override
    public boolean canGenerateNewResetToken(User user) {
        List<PasswordResetToken> userTokens = user.getPasswordResetTokens();
        if(userTokens.isEmpty()){
            return true;
        }
        for (PasswordResetToken token : userTokens) {
            if (isTokenUsedOrDateExpired(token)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isTokenUsedOrDateExpired(PasswordResetToken passwordResetToken){
        return (!passwordResetToken.isUsed() && passwordResetToken.getExpireDate().isAfter(LocalDateTime.now()));
    }

    @Override
    public boolean hasValidResetToken(PasswordResetToken passwordResetToken){
        return passwordResetToken != null && hasExpired(passwordResetToken.getExpireDate()) && !passwordResetToken.isUsed();
    }
}
