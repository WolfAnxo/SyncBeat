package com.example.SyncBeat.Controladores;

import com.example.SyncBeat.Entidades.Evento;
import com.example.SyncBeat.Entidades.ProyectoMusical;
import com.example.SyncBeat.Entidades.Rol;
import com.example.SyncBeat.Entidades.Usuario;
import com.example.SyncBeat.Servicios.EventoService;
import com.example.SyncBeat.Servicios.ProyectoMusicalService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/perfil-artista")
public class ArtistaController {

	@Autowired
	private ProyectoMusicalService proyectoMusicalService;
	@Autowired
	private EventoService eventoService;

	@GetMapping
	public String mostrarVistaArtistas(Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		if (usuario != null && usuario.getRol() == Rol.ARTISTA) {
			List<ProyectoMusical> proyectos = proyectoMusicalService.buscarProyectosPorArtista(usuario.getId());
			List<Evento> eventos = eventoService.buscarEventosPorArtista(usuario);
			model.addAttribute("proyectos", proyectos);
			model.addAttribute("eventos", eventos);
			model.addAttribute("usuario", usuario);
			return "artista";
		} else {
			return "redirect:/";
		}
	}

	@GetMapping("/proyectos")
	public String verMisProyectos(Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		if (usuario != null && usuario.getRol() == Rol.ARTISTA) {
			List<ProyectoMusical> proyectos = proyectoMusicalService.buscarProyectosPorArtista(usuario.getId());
			model.addAttribute("proyectos", proyectos);
			model.addAttribute("artista", usuario);
		}
		return "mis_proyectos";
	}

	@GetMapping("/eventos")
	public String verMisEventos(Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		if (usuario != null && usuario.getRol() == Rol.ARTISTA) {
			List<Evento> eventos = eventoService.buscarEventosPorArtista(usuario);
			model.addAttribute("eventos", eventos);
		}
		return "mis_eventos";
	}
}