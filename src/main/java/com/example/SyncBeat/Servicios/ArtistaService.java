package com.example.SyncBeat.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SyncBeat.Entidades.Rol;
import com.example.SyncBeat.Entidades.Usuario;
import com.example.SyncBeat.repositorios.UsuarioRepository; 

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistaService {

    @Autowired
    private UsuarioRepository usuarioRepository; 
    
    @Autowired
    private HttpSession session;

    public List<Usuario> listarArtistas() {
        return usuarioRepository.findByRol(Rol.ARTISTA); 
    }

    public Optional<Usuario> obtenerArtistaPorId(Long id) {
        return usuarioRepository.findById(id)
                .filter(usuario -> usuario.getRol() == Rol.ARTISTA); 
    }

    public Usuario guardarArtista(Usuario artista) {
        artista.setRol(Rol.ARTISTA); 
        return usuarioRepository.save(artista);
    }

    public void eliminarArtista(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Búsquedas
    public List<Usuario> buscarArtistasPorNombre(String nombre) {
        return usuarioRepository.findByNombreContainingIgnoreCaseAndRol(nombre, Rol.ARTISTA); 
    }
    
    public Usuario obtenerArtistaAutenticado() {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId != null) {
            return usuarioRepository.findById(usuarioId).orElse(null);
        } else {
            return null; 
        }
    }

    public Usuario buscarArtistaPorUsuario(Usuario usuario) {
        return usuario;
    }
}
