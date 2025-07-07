package com.example.SyncBeat.Servicios;

import com.example.SyncBeat.Entidades.Rol;
import com.example.SyncBeat.Entidades.Usuario;
import com.example.SyncBeat.repositorios.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private HttpSession session;

	public List<Usuario> listarUsuarios() {
		return usuarioRepository.findAll();
	}

	public Optional<Usuario> obtenerUsuarioPorId(Long id) {
		return usuarioRepository.findById(id);
	}

	@Transactional
	public Usuario guardarUsuario(Usuario usuario) {
		if (!validarContrasena(usuario.getContrasena())) {
            throw new RuntimeException("La contraseña no cumple con los requisitos de seguridad.");
        }
		try {
			return usuarioRepository.save(usuario);
		} catch (DataIntegrityViolationException e) {
			throw new RuntimeException("Error al guardar el usuario: " + e.getRootCause().getMessage());
		} catch (Exception e) {
			throw new RuntimeException("Error inesperado al guardar el usuario: " + e.getMessage());
		}
	}

	public void eliminarUsuario(Long id) {
		usuarioRepository.deleteById(id);
	}

	public Optional<Usuario> buscarUsuarioPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	public boolean autenticarUsuario(String email, String contrasena) {
		Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
		if (usuarioOptional.isPresent()) {
			Usuario usuario = usuarioOptional.get();
			return usuario.getContrasena().equals(contrasena);
		} else {
			return false;
		}
	}

	public Usuario obtenerUsuarioAutenticado() {
		Long usuarioId = (Long) session.getAttribute("usuarioId");
		if (usuarioId != null) {
			return usuarioRepository.findById(usuarioId).orElse(null);
		} else {
			return null;
		}
	}

	public List<Usuario> listarUsuariosPorRol(Rol rol) {
		return usuarioRepository.findByRol(rol);
	}

	public boolean existeOtroUsuarioConElMismoEmail(Long id, String email) {
		Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
		return usuarioOptional.isPresent() && !usuarioOptional.get().getId().equals(id);
	}
	
	private boolean validarContrasena(String contrasena) {
        if (contrasena.length() < 8) {
            return false;
        }
        boolean tieneMayuscula = false;
        boolean tieneMinuscula = false;
        boolean tieneDigito = false;
        boolean tieneCaracterEspecial = false;
        for (char c : contrasena.toCharArray()) {
            if (Character.isUpperCase(c)) {
                tieneMayuscula = true;
            } else if (Character.isLowerCase(c)) {
                tieneMinuscula = true;
            } else if (Character.isDigit(c)) {
                tieneDigito = true;
            } else {
                tieneCaracterEspecial = true;
            }
        }
        return tieneMayuscula && tieneMinuscula && tieneDigito && tieneCaracterEspecial;
    }
}
