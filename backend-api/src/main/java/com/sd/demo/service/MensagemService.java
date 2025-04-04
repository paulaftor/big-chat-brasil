package com.sd.demo.service;
import com.sd.demo.entity.Cliente;
import com.sd.demo.entity.Mensagem;
import com.sd.demo.entity.StatusMensagem;
import com.sd.demo.entity.TipoPlano;
import com.sd.demo.repository.MensagemRepository;

import com.sd.demo.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
@Service
public class MensagemService {

    private final MensagemRepository mensagemRepository;
    private final ClienteRepository ClienteRepository;
    private final Logger logger = LoggerFactory.getLogger(MensagemService.class);
    private final RabbitTemplate rabbitTemplate;
    private final String exchange = "mensagem-exchange";

    public MensagemService(MensagemRepository mensagemRepository, RabbitTemplate rabbitTemplate, ClienteRepository ClienteRepository) {
        this.mensagemRepository = mensagemRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.ClienteRepository = ClienteRepository;
    }

    public Mensagem salvarMensagem(Mensagem mensagem) {

        Cliente remetente = ClienteRepository.findById(mensagem.getCliente().getId()).orElse(null);

        Boolean clientePossuiSaldo = remetente.getPlano().equals(TipoPlano.PREPAGO) && remetente.getCreditos() >= 0.25;
        Boolean clientePossuiLimite = remetente.getPlano().equals(TipoPlano.POSPAGO) && remetente.getCreditos() < remetente.getLimiteConsumo();

        if(clientePossuiSaldo || clientePossuiLimite){

            mensagem.setDataEnvio(LocalDateTime.now());
            mensagem.setStatus(StatusMensagem.PENDENTE);
            Mensagem mensagemSalva = mensagemRepository.save(mensagem);
            logger.info("Mensagem salva no banco de dados: {}", mensagemSalva);
            rabbitTemplate.convertAndSend(exchange, "", mensagemSalva);
            logger.info("Mensagem enviada para o RabbitMQ.");

            if (clientePossuiLimite) {
                remetente.setCreditos(remetente.getCreditos() + 0.25);
            } else if (clientePossuiSaldo) {
                remetente.setCreditos(remetente.getCreditos() - 0.25);
            }

            ClienteRepository.save(remetente); 

            return mensagemSalva;
        }
        else{
            logger.info("Cliente não possui saldo ou limite para enviar a mensagem");
            return null;
        }
  
    }

    public Mensagem atualizarStatus(Mensagem mensagem, StatusMensagem status) {
       mensagem.setStatus(status);
       return mensagemRepository.save(mensagem);
    }
    
    public List<Mensagem> listarMensagens() {
        return mensagemRepository.findAllByOrderByDataEnvioAsc(); // Agora as mensagens são ordenadas
    }

    public List<Mensagem> buscarMensagemPorCliente(Long id) {
        return mensagemRepository.findAllByClienteIdOrderByDataEnvioAsc(id);
    }

    public boolean deletarMensagem(Long id) {
        if (mensagemRepository.existsById(id)) {
            mensagemRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

