package com.sd.email.dto;

import java.time.LocalDateTime;

public class Mensagem {

    private Long id;

    private String conteudo;

    private String telefoneDestinatario;

    private boolean flWhatsapp;

    private Cliente cliente;

    private LocalDateTime dataEnvio;

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
