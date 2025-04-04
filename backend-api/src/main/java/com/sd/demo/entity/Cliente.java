package com.sd.demo.entity;

import jakarta.persistence.*;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String telefone;
    
    private String senha;

    @Column(unique = true, nullable = false)
    private String cpfResponsavel;

    @Column(unique = true, nullable = false)
    private String cnpj;

    @Enumerated(EnumType.STRING)
    private TipoPlano plano;

    private double creditos;  
    private double limiteConsumo; 

    public Cliente() {
    }

    public Cliente(String nome, String email, String telefone, String cpfResponsavel, String cnpj, TipoPlano plano, double creditos, double limiteConsumo) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cpfResponsavel = cpfResponsavel;
        this.cnpj = cnpj;
        this.plano = plano;
        this.creditos = creditos;
        this.limiteConsumo = limiteConsumo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpfResponsavel() {
        return cpfResponsavel;
    }

    public void setCpfResponsavel(String cpfResponsavel) {
        this.cpfResponsavel = cpfResponsavel;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public TipoPlano getPlano() {
        return plano;
    }

    public void setPlano(TipoPlano plano) {
        this.plano = plano;
    }

    public double getCreditos() {
        return creditos;
    }

    public void setCreditos(double creditos) {
        this.creditos = creditos;
    }

    public double getLimiteConsumo() {
        return limiteConsumo;
    }

    public void setLimiteConsumo(double limiteConsumo) {
        this.limiteConsumo = limiteConsumo;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    
}
