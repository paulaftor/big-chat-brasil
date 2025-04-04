package com.sd.demo.controller;

import com.sd.demo.entity.Mensagem;
import com.sd.demo.entity.StatusMensagem;
import com.sd.demo.service.ClienteService;
import com.sd.demo.service.MensagemService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/mensagens")
public class MensagemController {
    private final MensagemService mensagemService;
    private final ClienteService clienteService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // injeta o serviço
    public MensagemController(MensagemService mensagemService, ClienteService clienteService) {
        this.mensagemService = mensagemService;
        this.clienteService = clienteService;
    }

    // cria uma nova mensagem (POST)
    @PostMapping("/enviar")
    public ResponseEntity<Mensagem> salvarMensagem(@RequestBody Mensagem mensagem) {
        Mensagem mensagemSalva = mensagemService.salvarMensagem(mensagem);
        messagingTemplate.convertAndSend("/topic/mensagens", mensagem);
        return ResponseEntity.ok(mensagemSalva); // retorna a mensagem salva para o frontend
    }

    // recebendo a resposta que a mensagem foi processada
    @RabbitListener(queues = "resposta-queue")
    public void receberResposta(Mensagem mensagem) {

        if(mensagem.getStatus().equals(StatusMensagem.PROCESSADO)){
            System.out.println("Mensagem recebida: " + mensagem.getConteudo());
            
        }
        else if(mensagem.getStatus().equals(StatusMensagem.ERRO)){
            System.out.println("Erro ao enviar a mensagem: " + mensagem.getConteudo());
            // Estorna o saldo do cliente 
            clienteService.estornarSaldo(mensagem.getCliente());
        }

        mensagemService.atualizarStatus(mensagem, StatusMensagem.PROCESSADO);
        
    }

    // busca todas as mensagens (GET)
    @GetMapping
    public ResponseEntity<List<Mensagem>> listarMensagens() {
        List<Mensagem> mensagens = mensagemService.listarMensagens();
        return ResponseEntity.ok(mensagens);
    }

    // busca mensagens de um determinado cliente 
    @GetMapping("/{id}")
    public ResponseEntity<List<Mensagem>> buscarMensagensCliente(@PathVariable Long id) {
        List<Mensagem> mensagem = mensagemService.buscarMensagemPorCliente(id);
        if (mensagem != null) {
            return ResponseEntity.ok(mensagem);
        }
        else{
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
