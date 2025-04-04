package com.sd.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String conteudo;

    @Column(name = "telefone_destinatario", nullable = false)
    private String telefoneDestinatario;

    @Column(name = "fl_whatsapp")
    private boolean flWhatsapp;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    @Enumerated(EnumType.STRING)
    private StatusMensagem status;

    public Mensagem() {
    }

    public Mensagem(String conteudo, String telefoneDestinatario, boolean isWhatsapp, Cliente cliente, LocalDateTime dataEnvio, StatusMensagem status) {
        this.conteudo = conteudo;
        this.telefoneDestinatario = telefoneDestinatario;
        this.flWhatsapp = isWhatsapp;
        this.cliente = cliente;
        this.dataEnvio = dataEnvio;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getTelefoneDestinatario() {
        return telefoneDestinatario;
    }

    public void setTelefoneDestinatario(String telefoneDestinatario) {
        this.telefoneDestinatario = telefoneDestinatario;
    }

    public boolean getFlWhatsapp() {
        return this.flWhatsapp;
    }

    public void setFlWhatsapp(boolean whatsapp) {
        this.flWhatsapp = whatsapp;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public StatusMensagem getStatus() {
        return status;
    }

    public void setStatus(StatusMensagem status) {
        this.status = status;
    }
}
