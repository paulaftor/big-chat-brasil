package com.sd.demo.controller;

import com.sd.demo.dto.Login;
import com.sd.demo.dto.LoginResponse;
import com.sd.demo.dto.ErrorResponse;
import com.sd.demo.entity.Usuario;
import com.sd.demo.service.JwtService;
import com.sd.demo.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;  // Injetando o JwtService

    public UsuarioController(UsuarioService usuarioService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Usuario> salvarUsuario(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.salvarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/autenticar")
    public ResponseEntity<?> autenticar(@RequestBody Login dadosLogin) {
        System.out.println("Dados de Login recebidos: " + dadosLogin.getInput() + " senha = " + dadosLogin.getSenha());

        if (dadosLogin == null || dadosLogin.getInput() == null || dadosLogin.getSenha() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Dados de login inválidos", 400));
        }

        Usuario usuario = usuarioService.autenticar(dadosLogin.getInput(), dadosLogin.getSenha());
        if (usuario != null) {
            String token = jwtService.gerarToken(usuario);
            return ResponseEntity.ok(new LoginResponse(token, usuario.getId(), usuario.getUsername()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Credenciais inválidas", 401));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        boolean foiDeletada = usuarioService.deletarUsuario(id);
        if (foiDeletada) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PatchMapping("/alterar-senha")
    public ResponseEntity<Usuario> atualizarSenhaUsuario(@RequestBody Login dados) {
        Usuario usuario = usuarioService.atualizarSenhaUsuario(dados);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
