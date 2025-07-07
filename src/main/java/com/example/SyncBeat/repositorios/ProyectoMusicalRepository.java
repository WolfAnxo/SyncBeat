package com.example.SyncBeat.repositorios;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SyncBeat.Entidades.ProyectoMusical;

import java.util.List;

@Repository
public interface ProyectoMusicalRepository extends JpaRepository<ProyectoMusical, Long> {
    List<ProyectoMusical> findByNombreContainingIgnoreCase(String nombre); 
    List<ProyectoMusical> findByArtistaId(Long artistaId); 
    List<ProyectoMusical> findByProductorId(Long productorId); 
}
