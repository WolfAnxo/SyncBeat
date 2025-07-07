package com.example.SyncBeat.repositorios;

import com.example.SyncBeat.Entidades.Evento;
import com.example.SyncBeat.Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByNombreContainingIgnoreCase(String nombre);
    List<Evento> findByFechaHoraBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<Evento> findByArtista(Usuario artista);
}
