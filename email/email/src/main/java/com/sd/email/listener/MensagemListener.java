package com.sd.email.listener;

import com.sd.email.dto.Mensagem;
import com.sd.email.dto.StatusMensagem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class MensagemListener {
    private final Logger logger = LoggerFactory.getLogger(MensagemListener.class);
    private final RabbitTemplate rabbitTemplate = new RabbitTemplate();

    @RabbitListener(queues = "mensagem-queue")
    public void receberMensagem(Mensagem mensagem) {

        logger.info("Tentando consumir a mensagem...");

        if(mensagem.getConteudo() == null){
            logger.info("Mensagem nula.");
            mensagem.setStatus(StatusMensagem.ERRO);  
            rabbitTemplate.convertAndSend("resposta-exchange", "", mensagem);
        }
        else{
            logger.info("Mensagem recebida: {}", mensagem.toString());
            logger.info("Enviando mensagem ao número ", mensagem.getTelefoneDestinatario());
            logger.info("...");
            mensagem.setStatus(StatusMensagem.PROCESSADO);  
            rabbitTemplate.convertAndSend("resposta-exchange", "", mensagem);
        }

    }
}
