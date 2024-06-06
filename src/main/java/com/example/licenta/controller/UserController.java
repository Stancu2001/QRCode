package com.example.licenta.controller;

import com.example.licenta.dto.ChangeRolesDTO;
import com.example.licenta.dto.EmailResetPasswordDTO;
import com.example.licenta.dto.ResetPasswordDTO;
import com.example.licenta.entities.PasswordResetToken;
import com.example.licenta.entities.User;
import com.example.licenta.exception.ResourceNotFoundException;
import com.example.licenta.exception.UnauthorizedAccesException;
import com.example.licenta.service.PasswordResetTokenServiceImpl;
import com.example.licenta.service.UserService;
import com.example.licenta.service.UserServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Value("${spring.mail.username}")
    private String senderEmail;
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender javaMailSender;

    private final UserServiceImpl userServiceImpl;
    private final PasswordResetTokenServiceImpl passwordResetTokenService;

    public UserController(UserServiceImpl userServiceImpl, PasswordResetTokenServiceImpl passwordResetTokenService) {
        this.userServiceImpl = userServiceImpl;
        this.passwordResetTokenService = passwordResetTokenService;
    }

//    @PostMapping("/register")
//    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO createUserDTO){
//        boolean createUser = userService.createUser(createUserDTO);
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setFrom(senderEmail);
//        msg.setTo(user.getEmail());
//
//        msg.setSubject("Reset Password");
//        LocalDateTime dateTime = LocalDateTime.now().plusMinutes(30);
//        String formattedTime = dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
//        msg.setText("Hello " + user.getFirstName() + " " + user.getLastName() + "\n\n" + "Please click on this link to reset your Password: " + resetLink + resetToken.getToken() + " . \n\n" + "This link will automatically expire on the hour: " + formattedTime);
//
//        javaMailSender.send(msg);
//        if(!createUser){
//
//            return ResponseEntity.internalServerError().build();
//        }
//        return ResponseEntity.ok().build();
//    }
//    @GetMapping
//    public ResponseEntity<?> test() throws MessagingException {
//        Context context = new Context();
////        String link="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSL2qjGS6C-MNJTI3Z59DKTx3dOVpp8NfVW1jvkDQNZ0g&s";
//        context.setVariable("name", "test");
////        context.setVariable("link", link);
//
//        String process = templateEngine.process("email-template.html", context);
//
//        MimeMessageHelper message = new MimeMessageHelper(javaMailSender.createMimeMessage());
//        message.setFrom(senderEmail);
//        message.setTo("alex.am707@gmail.com");
//        message.setSubject("Confirmare cont");
//        message.setText(process, true);
//
//        javaMailSender.send(message.getMimeMessage());
//        return ResponseEntity.ok("Email-ul a fost trimis");
//    }



    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPasswordProcess(@RequestBody EmailResetPasswordDTO emailResetPassword) {
        Optional<User> user = userServiceImpl.findUserByEmail(emailResetPassword.getEmail());
        if(user.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        boolean existTicket =passwordResetTokenService.canGenerateNewResetToken(user.get());
        if(!existTicket){
            return ResponseEntity.badRequest().body("Ai un ticket neutilizat");
        }

        User getuser=user.get();
        boolean sendEmail = passwordResetTokenService.sendEmail(getuser);
        if (!sendEmail) {
            return ResponseEntity.internalServerError().body("Din pacate a fost o eroare la procesarea cererii.");
        }
        return ResponseEntity.ok().body("Un email ti-a fost trimis.");
    }

    @GetMapping("/resetPassword/{token}")
    public  ResponseEntity<?> resetPasswordForm(@PathVariable String token) {
        PasswordResetToken reset = passwordResetTokenService.findByToken(token);
        if(reset==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket-ul nu exista");
        }

        if (passwordResetTokenService.hasValidResetToken(reset)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ticket-ul a expirat sau a fost utilizat");
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> passwordResetProcess(@RequestBody ResetPasswordDTO resetPassword) {
        try {
            PasswordResetToken reset = passwordResetTokenService.findByToken(resetPassword.getToken());

            if (reset == null) {
                throw new ResourceNotFoundException("Ticket-ul nu exista");
            }

            if (!passwordResetTokenService.hasValidResetToken(reset)) {
                throw new UnauthorizedAccesException("Token-ul a fost folosit sau a expirat");
            }

            User getuser = reset.getUser();
            getuser.setPassword(resetPassword.getPassword());
            userServiceImpl.saveUser(getuser);

            reset.setUsed(true);
            passwordResetTokenService.saveToken(reset);

            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (UnauthorizedAccesException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("A apărut o eroare la procesarea cererii: " + ex.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/changeRoles")
    public ResponseEntity<?> changeRoles(@Valid @RequestBody ChangeRolesDTO changeRolesDTO){
        userServiceImpl.changeRoles(changeRolesDTO);
        return ResponseEntity.ok().build();
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/setRoles")
    public ResponseEntity<?> changeRoles(){
        userServiceImpl.changeRoles();
        return ResponseEntity.ok().build();
    }
}
