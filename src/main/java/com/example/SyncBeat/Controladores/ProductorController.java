package com.example.SyncBeat.Controladores;

import com.example.SyncBeat.Entidades.ProyectoMusical;
import com.example.SyncBeat.Entidades.Rol;
import com.example.SyncBeat.Entidades.Usuario;
import com.example.SyncBeat.Servicios.ProyectoMusicalService;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/productor")
public class ProductorController {

    @Autowired
    private ProyectoMusicalService proyectoMusicalService;

    @GetMapping
    public String mostrarVistaProductor(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null && usuario.getRol() == Rol.PRODUCTOR) {
            List<ProyectoMusical> proyectos = proyectoMusicalService.buscarProyectosPorProductor(usuario.getId());
            model.addAttribute("proyectos", proyectos);
            model.addAttribute("usuario", usuario);
            return "productor";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/proyectos")
    public String verMisProyectos(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null && usuario.getRol() == Rol.PRODUCTOR) {
            List<ProyectoMusical> proyectos = proyectoMusicalService.buscarProyectosPorProductor(usuario.getId());
            model.addAttribute("proyectos", proyectos);
            model.addAttribute("productor", usuario);
        }
        return "mis_proyectos_productor";
    }
}
