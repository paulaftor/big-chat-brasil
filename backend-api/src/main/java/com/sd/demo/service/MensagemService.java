package com.sd.demo.service;
import com.sd.demo.dto.NotificacaoMensagem;
import com.sd.demo.entity.Mensagem;
import com.sd.demo.repository.MensagemRepository;
import com.sd.demo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class MensagemService {

    private final MensagemRepository mensagemRepository;
    private final UsuarioRepository usuarioRepository;
    private final Logger logger = LoggerFactory.getLogger(MensagemService.class);
    private final RabbitTemplate rabbitTemplate;
    private final String exchange = "mensagem-exchange";

    public MensagemService(MensagemRepository mensagemRepository, RabbitTemplate rabbitTemplate, UsuarioRepository usuarioRepository) {
        this.mensagemRepository = mensagemRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.usuarioRepository = usuarioRepository;
    }

    public Mensagem salvarMensagem(Mensagem mensagem) {
        mensagem.setDataEnvio(LocalDateTime.now());
        Mensagem mensagemSalva = mensagemRepository.save(mensagem);
        logger.info("Mensagem salva no banco de dados: {}", mensagemSalva);
        NotificacaoMensagem notificacao = new NotificacaoMensagem(mensagemSalva.getConteudo(),mensagemSalva.getRemetente(),usuarioRepository.findEmails());
        rabbitTemplate.convertAndSend(exchange, "", notificacao);
        logger.info("Mensagem enviada para o RabbitMQ: {}", notificacao);
        return mensagemSalva;
    }

    public List<Mensagem> listarMensagens() {
        return mensagemRepository.findAllByOrderByDataEnvioAsc(); // Agora as mensagens são ordenadas
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

