package com.sd.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sd.demo.entity.Mensagem;
import java.util.List;

public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
    List<Mensagem> findAllByOrderByDataEnvioAsc(); 
    
    // Encontrar todas as mensagens enviadas por um cliente ordenadas por dataEnvio
    List<Mensagem> findAllByClienteIdOrderByDataEnvioAsc(Long clienteId);
}
