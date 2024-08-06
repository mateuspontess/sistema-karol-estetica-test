package br.com.karol.sistema.business.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import br.com.karol.sistema.api.mapper.EmailMapper;
import br.com.karol.sistema.domain.valueobjects.Email;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailSendService {
    
    private final String remetente = "mateuspsdd@gmail.com";
    private final JavaMailSender javaMailSender;
    private final EmailMapper emailMapper;
    private final TokenService tokenService;


    public void sendEmailVerification(String input) {
        Email email = emailMapper.toEmail(input); // valida o email
        String emailToken = tokenService.generateToken(email.getValue());

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setFrom(this.remetente);
        emailMessage.setSubject("Verificação de email");
        emailMessage.setText(emailToken);
        emailMessage.setTo(email.getValue());
        
        javaMailSender.send(emailMessage);
    }

    @Async
    public void sendEmailAsync(SimpleMailMessage mailMessage) {
        javaMailSender.send(mailMessage);
    }
}