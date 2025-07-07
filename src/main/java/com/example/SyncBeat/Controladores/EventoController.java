package com.example.SyncBeat.Controladores;

import com.example.SyncBeat.Entidades.Evento;
import com.example.SyncBeat.Entidades.Rol;
import com.example.SyncBeat.Entidades.Usuario;
import com.example.SyncBeat.Servicios.EventoService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @Autowired
    private HttpSession session;

    @GetMapping
    public String listarEventos(Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            List<Evento> eventos;
            if (usuario.getRol() == Rol.ARTISTA) {
                eventos = eventoService.buscarEventosPorArtista(usuario);
            } else {
                eventos = eventoService.listarEventos();
            }
            model.addAttribute("eventos", eventos);
            model.addAttribute("rol", usuario.getRol().toString());
            if (usuario.getRol() == Rol.ARTISTA) {
                return "mis_eventos";
            } else {
                return "gestion_eventos";
            }
        } else {
            return "redirect:/login"; // Vuelve al login si no se autentifica correctamente
        }
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoEvento(Model model) {
        model.addAttribute("evento", new Evento());
        return "formulario_evento";
    }

    @PostMapping("/guardar")
    public String crearEvento(@ModelAttribute Evento evento, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión para crear un evento.");
            return "redirect:/login";
        }

        try {
            evento.setArtista(usuario); // Asigna el usuario autenticado como el artista del evento
            eventoService.guardarEvento(evento);
            redirectAttributes.addFlashAttribute("mensaje", "Evento creado exitosamente");
            return "redirect:/eventos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el evento: " + e.getMessage());
            return "formulario_evento";
        }
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditarEvento(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión para editar un evento.");
            return "redirect:/login";
        }

        Evento evento = eventoService.obtenerEventoPorId(id).orElse(null);
        if (evento == null || !evento.getArtista().equals(usuario)) {
            redirectAttributes.addFlashAttribute("error", "No tiene permiso para editar este evento.");
            return "redirect:/eventos";
        }

        model.addAttribute("evento", evento);
        return "formulario_evento";
    }

    @PostMapping("/{id}/guardar")
    public String actualizarEvento(@PathVariable Long id, @ModelAttribute Evento evento, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión para editar un evento.");
            return "redirect:/login";
        }

        Evento eventoExistente = eventoService.obtenerEventoPorId(id).orElse(null);
        if (eventoExistente == null || !eventoExistente.getArtista().equals(usuario)) {
            redirectAttributes.addFlashAttribute("error", "No tiene permiso para editar este evento.");
            return "redirect:/eventos";
        }

        try {
            evento.setId(id);
            evento.setArtista(usuario); 
            eventoService.guardarEvento(evento);
            redirectAttributes.addFlashAttribute("mensaje", "Evento actualizado exitosamente");
            return "redirect:/eventos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el evento: " + e.getMessage());
            return "formulario_evento";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarEvento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión para eliminar un evento.");
            return "redirect:/login";
        }

        Evento evento = eventoService.obtenerEventoPorId(id).orElse(null);
        if (evento == null || !evento.getArtista().equals(usuario)) {
            redirectAttributes.addFlashAttribute("error", "No tiene permiso para eliminar este evento.");
            return "redirect:/eventos";
        }

        try {
            eventoService.eliminarEvento(id);
            redirectAttributes.addFlashAttribute("mensaje", "Evento eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el evento: " + e.getMessage());
        }

        return "redirect:/eventos";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evento> obtenerEventoPorId(@PathVariable Long id) {
        return eventoService.obtenerEventoPorId(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evento> actualizarEvento(@PathVariable Long id, @RequestBody Evento evento) {
        if (!eventoService.obtenerEventoPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        evento.setId(id);
        Evento eventoActualizado = eventoService.guardarEvento(evento);
        return ResponseEntity.ok(eventoActualizado);
    }
}
