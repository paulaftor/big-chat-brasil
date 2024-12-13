package com.sd.email.service;
import com.sd.email.dto.NotificacaoMensagem;
import com.sd.email.dto.NotificacaoUsuario;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
                String assunto = "Bem-vindo ao Chat App!"; // Assunto com emoji

                String corpo = "<html><body>" +
                        "<h2>Uhuuul! &#x1F60E; <strong>" + notificacao.getNovoUsuario() + "</strong> acabou de entrar!</h2>" +
                        "<p>Agora temos um novo membro na nossa comunidade! Vamos dar as boas-vindas a ele! &#x1F44B;&#x1F3F8;</p>" +
                        "<p>Estamos muito felizes em ter você com a gente. Prepare-se para uma experiência incrível! &#x1F609;</p>" +
                        "<br><br>" +
                        "<p><em>Equipe Chat App</em></p>" +
                        "</body></html>";

                enviarEmail(email, assunto, corpo);

            }
        }
    }

    // Envio de e-mails para todos os usuários (para NotificacaoMensagem)
    public void enviarEmailTodosUsuarios(NotificacaoMensagem notificacao) {
        List<String> destinatarios = notificacao.getEmailsUsuarios();
        for (String email : destinatarios) {
            String assunto = "Você tem uma nova mensagem!"; // Assunto com emoji de mensagem

            String corpo = "<html><body>" +
                    "<h2>Você recebeu uma nova mensagem! &#x1F4AC;</h2>" +
                    "<p><strong>" + notificacao.getRemetente() + "</strong> enviou uma mensagem para você:</p>" +
                    "<blockquote style='border-left: 4px solid #ddd; padding-left: 10px; font-style: italic;'>" +
                    notificacao.getMensagem() +
                    "</blockquote>" +
                    "<p>Para responder, basta acessar o Chat App! &#x1F91D;</p>" +
                    "<br><br>" +
                    "<p>Até logo! &#x1F64B;</p>" +
                    "<p><em>Equipe Chat App</em></p>" +
                    "</body></html>";


            enviarEmail(email, assunto, corpo);
        }
    }


    public void enviarEmail(String to, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage(); // Criar o MimeMessage
        try {
            // Criar o MimeMessageHelper com o MimeMessage criado
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Definindo o remetente, destinatário e assunto
            helper.setFrom("noreply@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);

            // Definir o corpo como HTML
            helper.setText(body, true); // O segundo parâmetro true indica que é HTML

            // Enviar o e-mail
            mailSender.send(message);
            logger.info("Email enviado com sucesso para: {}", to);
        } catch (MessagingException e) {
            logger.error("Erro ao enviar o e-mail: ", e);
        }
    }


}
