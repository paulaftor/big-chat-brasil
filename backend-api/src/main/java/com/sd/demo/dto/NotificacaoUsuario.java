package com.sd.demo.dto;

import java.util.List;

public class NotificacaoUsuario {
    private String novoUsuario;
    private List<String> emailsUsuarios;

    public NotificacaoUsuario() {
    }

    public NotificacaoUsuario(String novoUsuario, List<String> emailsUsuarios) {
        this.novoUsuario = novoUsuario;
        this.emailsUsuarios = emailsUsuarios;
    }

    public String getNovoUsuario() {
        return novoUsuario;
    }

    public void setNovoUsuario(String novoUsuario) {
        this.novoUsuario = novoUsuario;
    }

    public List<String> getEmailsUsuarios() {
        return emailsUsuarios;
    }

    public void setEmailsUsuarios(List<String> emailsUsuarios) {
        this.emailsUsuarios = emailsUsuarios;
    }

}
