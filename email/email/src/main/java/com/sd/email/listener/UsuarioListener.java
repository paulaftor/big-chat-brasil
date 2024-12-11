package com.sd.email.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sd.email.dto.NotificacaoMensagem;
import com.sd.email.dto.NotificacaoUsuario;
import com.sd.email.service.EmailService;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;

@Component
public class UsuarioListener {
    private final Logger logger = LoggerFactory.getLogger(UsuarioListener.class);

    private final EmailService emailService;

    public UsuarioListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "usuario-queue")
    public void receberMensagem(NotificacaoUsuario notificacao) {
        logger.info("Tentando consumir a mensagem...");

        if (notificacao == null) {
            logger.info("Mensagem nula");
            return;
        }

        emailService.enviarEmailTodosUsuarios(notificacao);
        logger.info("Notificação gerada: {}", notificacao.toString());
    }

    private NotificacaoUsuario deserialize(byte[] body) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(body, NotificacaoUsuario.class);
        } catch (IOException e) {
            logger.error("Erro ao desserializar a notificação: {}", e.getMessage());
            throw new RuntimeException("Falha na desserialização da notificação", e);
        }
    }


}
