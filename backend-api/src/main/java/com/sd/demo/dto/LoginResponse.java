package com.sd.demo.dto;

public class Login {
    private String input;
    private String senha;

    public Login() {
    }

    public Login(String input, String senha) {
        this.input = input;
        this.senha = senha;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Boolean inputIsEmail() {
        return input.matches("^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
