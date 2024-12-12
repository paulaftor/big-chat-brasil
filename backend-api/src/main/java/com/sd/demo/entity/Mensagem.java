package com.sd.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Mensagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String conteudo;

    @Column(name = "remetente_username")
    private String remetente;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    public Mensagem() {
    }

    public Mensagem(String conteudo, String remetente, LocalDateTime dataEnvio) {
        this.conteudo = conteudo;
        this.remetente = remetente;
        this.dataEnvio = dataEnvio;
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

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }
}
