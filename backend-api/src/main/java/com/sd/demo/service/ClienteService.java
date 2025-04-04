package com.sd.demo.service;

import com.sd.demo.dto.Login;
import com.sd.demo.dto.NotificacaoCliente;
import com.sd.demo.entity.Cliente;
import com.sd.demo.entity.TipoPlano;
import com.sd.demo.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final RabbitTemplate rabbitTemplate;
    private final PasswordEncoder passwordEncoder;
    private final String exchange = "Cliente-exchange";

    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    @Autowired
    public ClienteService(ClienteRepository clienteRepository, RabbitTemplate rabbitTemplate, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    public void estornarSaldo(Cliente cliente) {
        if (cliente == null || cliente.getPlano() == null) {
            throw new IllegalArgumentException("Cliente ou plano inválido");
        }
    
        if (cliente.getPlano().equals(TipoPlano.PREPAGO)) {
            cliente.setCreditos(cliente.getCreditos() + 0.25);
        } else if (cliente.getPlano().equals(TipoPlano.POSPAGO)) {
            cliente.setCreditos(cliente.getCreditos() - 0.25);
        }
    
        clienteRepository.save(cliente);
    }
    

    public Cliente autenticar(String dadosLogin, String senhaCliente) {
        Cliente Cliente;
        if (dadosLogin.matches(".*@.*")) {
            Cliente = clienteRepository.findByEmail(dadosLogin);
        } else {
            Cliente = clienteRepository.findByUsername(dadosLogin);
        }

        if (Cliente == null) {
            return null;
        }

        String senhaNoBanco = Cliente.getSenha();
        if (passwordEncoder.matches(senhaCliente, senhaNoBanco)) {
            return Cliente;
        } else {
            return null;
        }
    }

    public Cliente salvarCliente(Cliente Cliente) {
        if (Cliente.getSenha() == null || Cliente.getSenha().isEmpty()) {
            throw new IllegalArgumentException("A senha não pode ser vazia");
        }

        String senhaCriptografada = passwordEncoder.encode(Cliente.getSenha());
        Cliente.setSenha(senhaCriptografada);

        Cliente ClienteSalvo = clienteRepository.save(Cliente);
        logger.info("Cliente salvo no banco de dados: {}", ClienteSalvo);

        try {
            NotificacaoCliente notificacao = new NotificacaoCliente(ClienteSalvo.getNome(), clienteRepository.findEmails());
            rabbitTemplate.convertAndSend(exchange, "", notificacao);  

            logger.info("Notificação enviada para o RabbitMQ: {}", notificacao);
        } catch (Exception e) {
            logger.error("Erro ao enviar notificação para o RabbitMQ: {}", e.getMessage());
        }

        return ClienteSalvo;
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Cliente buscar(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    public boolean deletarCliente(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            logger.info("Usuário com ID {} deletado com sucesso.", id);
            return true;
        }
        logger.warn("Usuário com ID {} não encontrado.", id);
        return false;
    }

    public Boolean atualizar(Cliente cliente) {

        if (cliente.getId() == null) {
            logger.warn("ID do cliente não pode ser nulo.");
            return false;
        }

        clienteRepository.save(cliente);
        logger.info("Cliente atualizado: {}", cliente);
        return true;

    }

    public Cliente atualizarSenhaCliente(Login dados) {
        Cliente Cliente;

        if (dados.inputIsEmail()) {
            Cliente = clienteRepository.findByEmail(dados.getInput());
        } else {
            Cliente = clienteRepository.findByUsername(dados.getInput());
        }

        if (Cliente != null) {
            String senhaCriptografada = passwordEncoder.encode(dados.getSenha());
            Cliente.setSenha(senhaCriptografada);
            clienteRepository.save(Cliente);
            logger.info("Senha atualizada para o usuário: {}", Cliente.getNome());
        } else {
            logger.warn("Usuário não encontrado para atualização de senha: {}", dados.getInput());
        }

        return Cliente;
    }
}
