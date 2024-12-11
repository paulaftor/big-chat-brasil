package com.sd.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sd.demo.dto.Login;
import com.sd.demo.dto.NotificacaoUsuario;
import com.sd.demo.entity.Usuario;
import com.sd.demo.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RabbitTemplate rabbitTemplate;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String exchange = "usuario-exchange";

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, RabbitTemplate rabbitTemplate, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario salvarUsuario(Usuario usuario) {
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        logger.info("Usuario salvo no banco de dados: {}", usuarioSalvo);

        try {
            NotificacaoUsuario notificacao = new NotificacaoUsuario(usuarioSalvo.getUsername(), usuarioRepository.findEmails());
            String mensagem = objectMapper.writeValueAsString(notificacao);

            rabbitTemplate.convertAndSend(exchange, "", mensagem);
            logger.info("Notificação enviada para o RabbitMQ: {}", mensagem);
        } catch (Exception e) {
            logger.error("Erro ao enviar notificação para o RabbitMQ: {}", e.getMessage());
        }

        return usuarioSalvo;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscar(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario autenticar(String dadosLogin, String senhaUsuario) {
        Usuario usuario;

        if (dadosLogin.matches(".*@.*")) {
            usuario = usuarioRepository.findByEmail(dadosLogin);
        } else {
            usuario = usuarioRepository.findByUsername(dadosLogin);
        }

        if (usuario != null && passwordEncoder.matches(senhaUsuario, usuario.getSenha())) {
            logger.info("Usuário autenticado com sucesso: {}", usuario.getUsername());
            return usuario;
        }

        logger.warn("Falha na autenticação para: {}", dadosLogin);
        return null;
    }

    public boolean deletarUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            logger.info("Usuário com ID {} deletado com sucesso.", id);
            return true;
        }
        logger.warn("Usuário com ID {} não encontrado.", id);
        return false;
    }

    public Usuario atualizarSenhaUsuario(Login dados) {
        Usuario usuario;

        if (dados.inputIsEmail()) {
            usuario = usuarioRepository.findByEmail(dados.getInput());
        } else {
            usuario = usuarioRepository.findByUsername(dados.getInput());
        }

        if (usuario != null) {
            String senhaCriptografada = passwordEncoder.encode(dados.getSenha());
            usuario.setSenha(senhaCriptografada);
            usuarioRepository.save(usuario);
            logger.info("Senha atualizada para o usuário: {}", usuario.getUsername());
        } else {
            logger.warn("Usuário não encontrado para atualização de senha: {}", dados.getInput());
        }

        return usuario;
    }
}
