package com.sd.demo.controller;

import com.sd.demo.dto.Login;
import com.sd.demo.dto.LoginResponse;
import com.sd.demo.dto.ErrorResponse;
import com.sd.demo.entity.Cliente;
import com.sd.demo.service.JwtService;
import com.sd.demo.service.ClienteService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final JwtService jwtService;  

    public ClienteController(ClienteService clienteService, JwtService jwtService) {
        this.clienteService = clienteService;
        this.jwtService = jwtService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Cliente> salvarCliente(@RequestBody Cliente Cliente) {
        Cliente novoCliente = clienteService.salvarCliente(Cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Cliente> atualizarCliente(@RequestBody Cliente Cliente) {
        Boolean clienteAtualizado = clienteService.atualizar(Cliente);
        if (clienteAtualizado) {
            return ResponseEntity.ok(Cliente);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> Clientes = clienteService.listarClientes();
        return ResponseEntity.ok(Clientes);
    }

    @PostMapping("/autenticar")
    public ResponseEntity<?> autenticar(@RequestBody Login dadosLogin) {
        System.out.println("Dados de Login recebidos: " + dadosLogin.getInput() + " senha = " + dadosLogin.getSenha());

        if (dadosLogin == null || dadosLogin.getInput() == null || dadosLogin.getSenha() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Dados de login inválidos", 400));
        }

        Cliente Cliente = clienteService.autenticar(dadosLogin.getInput(), dadosLogin.getSenha());
        if (Cliente != null) {
            String token = jwtService.gerarToken(Cliente);
            return ResponseEntity.ok(new LoginResponse(token, Cliente.getId(), Cliente.getNome()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Credenciais inválidas", 401));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        boolean foiDeletada = clienteService.deletarCliente(id);
        if (foiDeletada) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PatchMapping("/alterar-senha")
    public ResponseEntity<Cliente> atualizarSenhaCliente(@RequestBody Login dados) {
        Cliente Cliente = clienteService.atualizarSenhaCliente(dados);
        if (Cliente != null) {
            return ResponseEntity.ok(Cliente);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
