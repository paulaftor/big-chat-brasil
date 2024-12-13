package com.sd.email.listener;

import com.sd.email.dto.NotificacaoMensagem;
import com.sd.email.dto.NotificacaoUsuario;
import com.sd.email.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MensagemListener {
    private final Logger logger = LoggerFactory.getLogger(MensagemListener.class);

    private final EmailService emailService;

    public MensagemListener(EmailService emailService) {
        this.emailService = emailService;
        emailService.enviarEmail("ra123565@uem.br", "Teste", "Teste");
    }

    @RabbitListener(queues = "mensagem-queue")
    public void receberMensagem(NotificacaoMensagem notificacao) {

        logger.info("Tentando consumir a mensagem...");

        if(notificacao==null){
            logger.info("Mensagem nula");
            return;
        }

        emailService.enviarEmailTodosUsuarios(notificacao);
        logger.info("Notificação gerada: {}", notificacao.toString());
    }
}
