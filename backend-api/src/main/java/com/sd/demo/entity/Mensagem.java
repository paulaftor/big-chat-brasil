package com.sd.demo.entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
public class Mensagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String conteudo;

    @ManyToOne
    private Usuario remetente;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    public Mensagem(String conteudo, Usuario usuario) {
        this.conteudo = conteudo;
        this.remetente = usuario;
        this.dataEnvio = LocalDateTime.now();

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

    public Usuario getRemetente(){
        return remetente;
    }

    public void setRemetente(Usuario usuario){
        this.remetente = usuario;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

}
