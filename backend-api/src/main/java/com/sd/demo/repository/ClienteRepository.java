package com.sd.demo.repository;

import com.sd.demo.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public  interface ClienteRepository extends JpaRepository<Cliente, Long>{
    public Cliente findByUsername(String username);
    public Cliente findByEmail(String email);

    public default List<String> findEmails(){
        return findAll().stream().map(Cliente::getEmail).collect(Collectors.toList());
    }
}
