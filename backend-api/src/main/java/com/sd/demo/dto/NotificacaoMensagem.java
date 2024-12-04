package com.sd.demo.dto;

public class NotificacaoMensagem {
    private String mensagem;
    private String remetente;

    public NotificacaoMensagem() {
    }

    public NotificacaoMensagem(String mensagem, String remetente) {
        this.mensagem = mensagem;
        this.remetente = remetente;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

}
