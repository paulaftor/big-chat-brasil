package com.sd.email.dto;

import java.util.List;

public class NotificacaoMensagem {
    private String mensagem;
    private String remetente;

    private List<String> emailsUsuarios;

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

    public List<String> getEmailsUsuarios() {
        return emailsUsuarios;
    }

    public void setEmailsUsuarios(List<String> emailsUsuarios) {
        this.emailsUsuarios = emailsUsuarios;
    }

}
