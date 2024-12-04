package com.sd.demo.controller;

import com.sd.demo.dto.Login;
import com.sd.demo.entity.Usuario;
import com.sd.demo.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    // injeta o serviço
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Usuario> salvarUsuario(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.salvarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    // busca todas as mensagens (GET)
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> mensagens = usuarioService.listarUsuarios();
        return ResponseEntity.ok(mensagens);
    }

    @GetMapping("/autenticar")
    public ResponseEntity<Boolean> autenticar(@RequestBody Login dadosLogin){
        Boolean autenticado = usuarioService.autenticar(dadosLogin.getInput(), dadosLogin.getSenha());
        return ResponseEntity.ok(autenticado);
    }
    // deleta uma usuario por id (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        boolean foiDeletada = usuarioService.deletarUsuario(id);
        if (foiDeletada) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
