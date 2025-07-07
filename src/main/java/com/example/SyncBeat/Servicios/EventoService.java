package com.example.SyncBeat.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SyncBeat.Entidades.Evento;
import com.example.SyncBeat.Entidades.Usuario;
import com.example.SyncBeat.repositorios.EventoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    public List<Evento> listarEventos() {
        return eventoRepository.findAll();
    }

    public Optional<Evento> obtenerEventoPorId(Long id) {
        return eventoRepository.findById(id);
    }

    public Evento guardarEvento(Evento evento) {
        return eventoRepository.save(evento);
    }

    public void eliminarEvento(Long id) {
        eventoRepository.deleteById(id);
    }

    // Búsquedas
    public List<Evento> buscarEventosPorNombre(String nombre) {
        return eventoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Evento> buscarEventosPorFecha(LocalDate fecha) {
        return eventoRepository.findByFechaHoraBetween(
            fecha.atStartOfDay(), fecha.plusDays(1).atStartOfDay()
        );
    }
    public List<Evento> buscarEventosPorArtista(Usuario artista) {
        return eventoRepository.findByArtista(artista);
    }
}
