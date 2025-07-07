package com.example.SyncBeat.repositorios;

import com.example.SyncBeat.Entidades.Rol;
import com.example.SyncBeat.Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByRol(Rol rol);
    List<Usuario> findByNombreContainingIgnoreCaseAndRol(String nombre, Rol rol);
}