package com.sd.demo.repository;

import com.sd.demo.entity.Mensagem;
import com.sd.demo.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public  interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    public Usuario findByUsername(String username);
    public Usuario findByEmail(String email);

    public default List<String> findEmails(){
        return findAll().stream().map(Usuario::getEmail).collect(Collectors.toList());
    }
}
