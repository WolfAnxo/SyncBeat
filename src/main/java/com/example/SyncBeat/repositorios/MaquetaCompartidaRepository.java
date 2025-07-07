package com.example.SyncBeat.repositorios;

import com.example.SyncBeat.Entidades.Maqueta;
import com.example.SyncBeat.Entidades.MaquetaCompartida;
import com.example.SyncBeat.Entidades.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaquetaCompartidaRepository extends JpaRepository<MaquetaCompartida, Long> {
    List<MaquetaCompartida> findByCompartidoConId(Long usuarioId);
    void deleteByMaqueta(Maqueta maqueta);
    boolean existsByMaquetaAndCompartidoCon(Maqueta maqueta, Usuario usuario); 
    List<MaquetaCompartida> findByMaqueta(Maqueta maqueta);
}
