package com.example.SyncBeat.Controladores;

import com.example.SyncBeat.Entidades.Rol;
import com.example.SyncBeat.Entidades.Usuario;
import com.example.SyncBeat.Servicios.UsuarioService;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/guardar")
    public String guardarUsuario(@Validated @ModelAttribute("usuario") Usuario usuario,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "formulario_usuario";
        }

        try {
            usuarioService.guardarUsuario(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario guardado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el usuario: " + e.getMessage());
        }

        return "redirect:/admin/" + usuario.getRol().toString().toLowerCase() + "/listar";
    }
    
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditarUsuario(@PathVariable Long id, Model model) {
        try {
            Usuario usuario = usuarioService.obtenerUsuarioPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

            model.addAttribute("usuario", usuario);
            model.addAttribute("roles", Rol.values());
            model.addAttribute("rolEditable", false);

            return "editar_usuario";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", "El usuario no existe");
            return "redirect:/admin/usuarios";
        } catch (Exception e) {
            model.addAttribute("error", "Error al obtener el usuario: " + e.getMessage());
            return "redirect:/admin/usuarios";
        }
    }

    @PostMapping("/{id}/guardareditado")
    public String actualizarUsuario(@PathVariable Long id, @Validated @ModelAttribute("usuario") Usuario usuario, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "editar_usuario";
        }

        try {
            usuario.setId(id);
            usuarioService.guardarUsuario(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el usuario: " + e.getMessage());
        }

        return "redirect:/admin/" + usuario.getRol().toString().toLowerCase() + "/listar";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioService.obtenerUsuarioPorId(id).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
            usuarioService.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado exitosamente");
            return "redirect:/admin/" + usuario.getRol().toString().toLowerCase() + "/listar";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "El usuario no existe");
            return "redirect:/admin/usuarios";
        }
    }
    
    @GetMapping("/artistas")
    public List<Usuario> listarArtistas() {
        return usuarioService.listarUsuariosPorRol(Rol.ARTISTA);
    }

    @GetMapping("/artistas/{id}")
    public ResponseEntity<Usuario> obtenerArtistaPorId(@PathVariable Long id) {
        return usuarioService.obtenerUsuarioPorId(id)
                .filter(usuario -> usuario.getRol() == Rol.ARTISTA)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
} 