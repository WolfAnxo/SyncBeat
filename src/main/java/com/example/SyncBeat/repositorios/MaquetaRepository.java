package com.example.SyncBeat.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SyncBeat.Entidades.Maqueta;


@Repository
public interface MaquetaRepository extends JpaRepository<Maqueta, Long> {
	    List<Maqueta> findByArtistaId(Long artistaId);
	    List<Maqueta> findByProductorId(Long productorId);
	}
	
