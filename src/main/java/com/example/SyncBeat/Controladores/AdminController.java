package com.example.SyncBeat.Controladores;

import com.example.SyncBeat.Entidades.*;
import com.example.SyncBeat.Servicios.*;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ProyectoMusicalService proyectoMusicalService;
    @Autowired
    private EventoService eventoService;
    @Autowired
    private MaquetaService maquetaService;

    //Metodo que muestra el panel del administrador
    @GetMapping
    public String mostrarVistaAdmin(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null && usuario.getRol() == Rol.ADMINISTRADOR) {
            model.addAttribute("usuario", usuario);
            return "administrador";
        } else {
            return "redirect:/";
        }
    }

    //Metodo para listar todos los artistas dado de alta en la BD
    @GetMapping("/artista/listar")
    public String mostrarVistaArtistas(Model model) {
        List<Usuario> artistas = usuarioService.listarUsuariosPorRol(Rol.ARTISTA);
        model.addAttribute("usuarios", artistas);
        model.addAttribute("rol", "Artistas");
        return "gestion_usuarios";
    }

    //Metodo para listar todos los productores dado de alta en la BD
    @GetMapping("/productor/listar")
    public String mostrarVistaProductores(Model model) {
        List<Usuario> productores = usuarioService.listarUsuariosPorRol(Rol.PRODUCTOR);
        model.addAttribute("usuarios", productores);
        model.addAttribute("rol", "Productores");
        return "gestion_usuarios";
    }

  //Metodo para listar todos los administradores dado de alta en la BD
    @GetMapping("/administrador/listar")
    public String mostrarVistaAdministradores(Model model) {
        List<Usuario> administradores = usuarioService.listarUsuariosPorRol(Rol.ADMINISTRADOR);
        model.addAttribute("usuarios", administradores);
        model.addAttribute("rol", "Administradores");
        return "gestion_usuarios";
    }

    //Listar Proyectos
    @GetMapping("/proyectos")
    public String mostrarVistaProyectos(Model model) {
        List<ProyectoMusical> proyectos = proyectoMusicalService.listarProyectosMusicales();
        model.addAttribute("proyectos", proyectos);
        return "gestion_proyectos";
    }

    //Listar Eventos
    @GetMapping("/eventos")
    public String mostrarVistaEventos(Model model) {
        List<Evento> eventos = eventoService.listarEventos();
        model.addAttribute("eventos", eventos);
        return "gestion_eventos";
    }

    //Listar Maquetas
    @GetMapping("/maquetas")
    public String mostrarVistaMaquetas(Model model) {
        List<Maqueta> maquetas = maquetaService.listarMaquetas();
        model.addAttribute("maquetas", maquetas);
        return "gestion_maquetas";
    }

    //Metodo para enseñar el html para añadir usurio
    @GetMapping("/usuarios/nuevo")
    public String mostrarFormularioNuevoUsuario(Model model, @RequestParam(value = "rol", required = false) String rol) {
        Usuario usuario = new Usuario();
        boolean rolEditable = true;

        if (rol != null) {
            try {
                Rol rolEnum = Rol.valueOf(rol.toUpperCase());
                usuario.setRol(rolEnum);
                rolEditable = false;
            } catch (IllegalArgumentException e) {
            }
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", Rol.values());
        model.addAttribute("rolEditable", rolEditable);

        return "formulario_usuario";
    }
} 

