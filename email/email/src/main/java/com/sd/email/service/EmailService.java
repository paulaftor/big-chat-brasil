package com.sd.email.service;
import com.sd.email.dto.NotificacaoMensagem;
import com.sd.email.dto.NotificacaoUsuario;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Envio de e-mails para todos os usuários (para NotificacaoUsuario)
    public void enviarEmailTodosUsuarios(NotificacaoUsuario notificacao) {
        List<String> destinatarios = notificacao.getEmailsUsuarios();
        if(destinatarios != null && !destinatarios.isEmpty()) {
            for (String email : destinatarios) {
                enviarEmail(email, "Dê boas-vindas ao novo usuário!", notificacao.getNovoUsuario() + " se juntou ao Chat! Dê um olá!");
            }
        }
    }

    // Envio de e-mails para todos os usuários (para NotificacaoMensagem)
    public void enviarEmailTodosUsuarios(NotificacaoMensagem notificacao) {
        List<String> destinatarios = notificacao.getEmailsUsuarios();
        for (String email : destinatarios) {
            enviarEmail(email, "Nova mensagem de " + notificacao.getRemetente(), notificacao.getRemetente() + " enviou uma mensagem no Chat: " + notificacao.getMensagem());
        }
    }

    // Método de envio de e-mail
    public void enviarEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);
            logger.info("Email enviado com sucesso para: {}", to);
        } catch (Exception e) {
            logger.error("Erro ao enviar o e-mail: ", e);
        }
    }
}
