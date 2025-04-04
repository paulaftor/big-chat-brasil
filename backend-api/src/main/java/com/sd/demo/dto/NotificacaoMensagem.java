package com.sd.demo.dto;

import com.sd.demo.entity.Cliente;

public class NotificacaoMensagem {
    private String mensagem;
    private Cliente remetente;
    private String destinatario;


    public NotificacaoMensagem() {
    }

    public NotificacaoMensagem(String mensagem, Cliente remetente, String destinatario) {
        this.mensagem = mensagem;
        this.remetente = remetente;
        this.destinatario = destinatario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Cliente getRemetente() {
        return remetente;
    }

    public void setRemetente(Cliente remetente) {
        this.remetente = remetente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

}
