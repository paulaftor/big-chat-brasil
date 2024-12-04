package com.sd.demo.service;
import com.sd.demo.entity.Mensagem;
import com.sd.demo.repository.MensagemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class MensagemService {

    private final MensagemRepository mensagemRepository;
    private final Logger logger = LoggerFactory.getLogger(MensagemService.class);
    private final RabbitTemplate rabbitTemplate;
    private final String exchange = "mensagem-exchange";

    public MensagemService(MensagemRepository mensagemRepository, RabbitTemplate rabbitTemplate) {
        this.mensagemRepository = mensagemRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Mensagem salvarMensagem(Mensagem mensagem) {
        Mensagem mensagemSalva = mensagemRepository.save(mensagem);
        logger.info("Mensagem salva no banco de dados: {}", mensagemSalva);
        rabbitTemplate.convertAndSend(exchange, "", mensagemSalva);
        logger.info("Mensagem enviada para o RabbitMQ: {}", mensagemSalva);
        return mensagemSalva;
    }

    public List<Mensagem> listarMensagens() {
        return mensagemRepository.findAll();
    }

    public Mensagem buscarMensagemPorId(Long id) {
        return mensagemRepository.findById(id).orElse(null);
    }

    public boolean deletarMensagem(Long id) {
        if (mensagemRepository.existsById(id)) {
            mensagemRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

