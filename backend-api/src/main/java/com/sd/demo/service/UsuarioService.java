package com.sd.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sd.demo.dto.Login;
import com.sd.demo.dto.NotificacaoUsuario;
import com.sd.demo.entity.Usuario;
import com.sd.demo.repository.UsuarioRepository;
import org.apache.qpid.proton.engine.Sasl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessageBuilder;


import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RabbitTemplate rabbitTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();
    private final String exchange = "usuario-exchange";

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    //private final RabbitTemplate rabbitTemplate;


    public UsuarioService(UsuarioRepository UsuarioRepository, RabbitTemplate rabbitTemplate) {
        this.usuarioRepository = UsuarioRepository;
        this.rabbitTemplate = rabbitTemplate;
    }


    public Usuario salvarUsuario(Usuario usuario) {
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        logger.info("Usuario salvo no banco de dados: {}", usuarioSalvo);

        NotificacaoUsuario notificacao = new NotificacaoUsuario(usuarioSalvo.getUsername(), usuarioRepository.findEmails());
        rabbitTemplate.convertAndSend(exchange, "", notificacao);
        logger.info("Notificação enviada para o RabbitMQ: {}", notificacao);
        return usuarioSalvo;
    }


    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscar(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public  Boolean autenticar(String dadosLogin, String senhaUsuario) {
        Usuario usuario;
        if(dadosLogin.matches(".*@.*")) {
            usuario = usuarioRepository.findByEmail(dadosLogin);
        } else {
            usuario = usuarioRepository.findByUsername(dadosLogin);
        }

        if (usuario == null)
            return false;

        String senhaNoBanco = usuario.getSenha();
        return passwordEncoder.matches(senhaUsuario, senhaNoBanco);
    }

    public boolean deletarUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Usuario atualizarSenhaUsuario(Login dados) {
        Usuario usuario;
        if(dados.inputIsEmail())
            usuario = usuarioRepository.findByEmail(dados.getInput());
        else
            usuario = usuarioRepository.findByUsername(dados.getInput());

        if (usuario != null) {
            String senhaCriptografada = passwordEncoder.encode(dados.getSenha());
            usuario.setSenha(senhaCriptografada);
            usuarioRepository.save(usuario);
        }

        return usuario;
    }

}

