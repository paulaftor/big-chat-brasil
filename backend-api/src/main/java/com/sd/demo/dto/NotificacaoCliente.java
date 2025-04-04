package com.sd.demo.dto;

import java.util.List;

public class NotificacaoCliente {
    private String novoCliente;
    private List<String> emailsClientes;

    public NotificacaoCliente() {
    }

    public NotificacaoCliente(String novoCliente, List<String> emailsClientes) {
        this.novoCliente = novoCliente;
        this.emailsClientes = emailsClientes;
    }

    public String getNovoCliente() {
        return novoCliente;
    }

    public void setNovoCliente(String novoCliente) {
        this.novoCliente = novoCliente;
    }

    public List<String> getEmailsClientes() {
        return emailsClientes;
    }

    public void setEmailsClientes(List<String> emailsClientes) {
        this.emailsClientes = emailsClientes;
    }

}
