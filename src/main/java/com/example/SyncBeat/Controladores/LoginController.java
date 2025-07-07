package com.example.SyncBeat.Controladores;

import com.example.SyncBeat.Entidades.Usuario;
import com.example.SyncBeat.Servicios.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/login")
	public String mostrarFormularioLogin() {
		return "login";
	}

	@PostMapping("/login")
	public String procesarLogin(@RequestParam String email, @RequestParam String contrasena, Model model,
			HttpSession session) {
		Optional<Usuario> usuarioOptional = usuarioService.buscarUsuarioPorEmail(email);

		if (usuarioOptional.isPresent() && usuarioService.autenticarUsuario(email, contrasena)) {
			Usuario usuario = usuarioOptional.get();
			session.setAttribute("usuario", usuario);

			// Redirigir según el rol del que logea
			switch (usuario.getRol()) {
			case ADMINISTRADOR:
				return "redirect:/admin";
			case PRODUCTOR:
				return "redirect:/productor";
			case ARTISTA:
				return "redirect:/perfil-artista";
			default:
				return "redirect:/login";
			}
		} else {
			model.addAttribute("error", "Correo electrónico o contraseña incorrectos.");
			return "login";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
}
