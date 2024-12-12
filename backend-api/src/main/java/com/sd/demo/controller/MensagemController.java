package com.sd.demo.controller;

import com.sd.demo.entity.Mensagem;
import com.sd.demo.service.MensagemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/mensagens")
public class MensagemController {
    private final MensagemService mensagemService;

    // injeta o serviço
    public MensagemController(MensagemService mensagemService) {
        this.mensagemService = mensagemService;
    }

    // cria uma nova mensagem (POST)
    @PostMapping
    public ResponseEntity<Mensagem> salvarMensagem(@RequestBody Mensagem mensagem) {
        Mensagem mensagemSalva = mensagemService.salvarMensagem(mensagem);
        return ResponseEntity.ok(mensagemSalva); // Retorna a mensagem salva para o frontend
    }

    // busca todas as mensagens (GET)
    @GetMapping
    public ResponseEntity<List<Mensagem>> listarMensagens() {
        List<Mensagem> mensagens = mensagemService.listarMensagens();
        return ResponseEntity.ok(mensagens);
    }

    // busca uma mensagem específica por id (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Mensagem> buscarMensagemPorId(@PathVariable Long id) {
        Mensagem mensagem = mensagemService.buscarMensagemPorId(id);
        if (mensagem != null) {
            return ResponseEntity.ok(mensagem);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // deleta uma mensagem por id (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMensagem(@PathVariable Long id) {
        boolean foiDeletada = mensagemService.deletarMensagem(id);
        if (foiDeletada) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
