package com.example.SyncBeat.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SyncBeat.Entidades.ProyectoMusical;
import com.example.SyncBeat.repositorios.ProyectoMusicalRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProyectoMusicalService {

	@Autowired
	private ProyectoMusicalRepository proyectoMusicalRepository;

	public List<ProyectoMusical> listarProyectosMusicales() {
		return proyectoMusicalRepository.findAll();
	}

	public Optional<ProyectoMusical> obtenerProyectoMusicalPorId(Long id) {
		return proyectoMusicalRepository.findById(id);
	}

	public ProyectoMusical guardarProyectoMusical(ProyectoMusical proyectoMusical) {
		return proyectoMusicalRepository.save(proyectoMusical);
	}

	public void eliminarProyectoMusical(Long id) {
		proyectoMusicalRepository.deleteById(id);
	}

	// Búsquedas
	public List<ProyectoMusical> buscarProyectosPorNombre(String nombre) {
		return proyectoMusicalRepository.findByNombreContainingIgnoreCase(nombre);
	}

	public List<ProyectoMusical> buscarProyectosPorArtista(Long artistaId) {
		return proyectoMusicalRepository.findByArtistaId(artistaId);
	}

	public List<ProyectoMusical> buscarProyectosPorProductor(Long productorId) {
		return proyectoMusicalRepository.findByProductorId(productorId);
	}

}
