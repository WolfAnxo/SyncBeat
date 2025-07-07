package com.example.SyncBeat.Controladores;

import com.example.SyncBeat.Entidades.Maqueta;
import com.example.SyncBeat.Entidades.MaquetaCompartida;
import com.example.SyncBeat.Entidades.Rol;
import com.example.SyncBeat.Entidades.Usuario;
import com.example.SyncBeat.Servicios.MaquetaCompartidaService;
import com.example.SyncBeat.Servicios.MaquetaService;
import com.example.SyncBeat.Servicios.ProyectoMusicalService;
import com.example.SyncBeat.Servicios.UsuarioService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/maquetas")
public class MaquetaController {

    @Autowired
    private MaquetaService maquetaService;

    @Autowired
    private ProyectoMusicalService proyectoMusicalService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private HttpSession session;
    
    @Autowired
    private MaquetaCompartidaService maquetaCompartidaService;

    @GetMapping
    public String listarMaquetas(Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            List<Maqueta> maquetas = maquetaService.obtenerMaquetasDeUsuario(usuario.getId(), usuario.getRol());
            model.addAttribute("maquetas", maquetas);
            model.addAttribute("rol", usuario.getRol().toString());
            List<Usuario> usuariosParaCompartir = usuarioService.listarUsuarios()
                    .stream()
                    .filter(u -> !u.getId().equals(usuario.getId()) &&
                            (usuario.getRol() == Rol.ARTISTA ? u.getRol() != Rol.ADMINISTRADOR : u.getRol() != Rol.ADMINISTRADOR))
                    .collect(Collectors.toList());
            model.addAttribute("usuarios", usuariosParaCompartir);

            if (usuario.getRol() == Rol.ARTISTA) {
                return "mis_maquetas";
            } else if (usuario.getRol() == Rol.PRODUCTOR) {
                return "mis_maquetas_productor";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevaMaqueta(Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            model.addAttribute("maqueta", new Maqueta());
            model.addAttribute("proyectosMusicales", proyectoMusicalService.buscarProyectosPorArtista(usuario.getId()));
            return "formulario_maqueta";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/nuevo")
    public String crearMaqueta(@Validated @ModelAttribute("maqueta") Maqueta maqueta,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println("Error: " + error.getDefaultMessage());
            });
            return "formulario_maqueta";
        }

        try {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            maqueta.setArtista(usuario);

            // Metodo para convertir archivo de musica MultipartFile a byte
            if (maqueta.getArchivo() != null && !maqueta.getArchivo().isEmpty()) {
                maqueta.setArchivoAudio(maqueta.getArchivo().getBytes());
            }

            maquetaService.guardarMaqueta(maqueta);
            redirectAttributes.addFlashAttribute("mensaje", "Maqueta creada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la maqueta: " + e.getMessage());
        }

        return "redirect:/maquetas";
    }
    
    @PostMapping("/{id}/compartir")
    public String compartirMaqueta(@PathVariable Long id,
                                   @RequestParam("usuarioId") Long usuarioId,
                                   RedirectAttributes redirectAttributes) {
        try {
            Maqueta maqueta = maquetaService.obtenerMaquetaPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Maqueta no encontrada"));

            Usuario usuarioCompartidoCon = usuarioService.obtenerUsuarioPorId(usuarioId)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

            // Metodo para no compartir maqueta con el mismo que la creo
            Usuario usuarioAutenticado = (Usuario) session.getAttribute("usuario");
            if (usuarioAutenticado == null || usuarioAutenticado.getRol() != Rol.ARTISTA) {
                throw new Exception("No tienes permiso para compartir maquetas");
            }

            if (usuarioAutenticado.equals(usuarioCompartidoCon)) {
                redirectAttributes.addFlashAttribute("error", "No puedes compartir una maqueta contigo mismo.");
                return "redirect:/maquetas";
            }

            // Comprobar que no se comparte con un mismo usuario con el que se compartio
            if (maquetaCompartidaService.existeMaquetaCompartida(maqueta, usuarioCompartidoCon)) {
                redirectAttributes.addFlashAttribute("error", "La maqueta ya está compartida con este usuario.");
                return "redirect:/maquetas";
            }

            // Crear nuevo despliegue
            MaquetaCompartida maquetaCompartida = new MaquetaCompartida();
            maquetaCompartida.setMaqueta(maqueta);
            maquetaCompartida.setCompartidoPor(usuarioAutenticado);
            maquetaCompartida.setCompartidoCon(usuarioCompartidoCon);
            maquetaCompartidaService.guardarMaquetaCompartida(maquetaCompartida);

            redirectAttributes.addFlashAttribute("mensaje", "Maqueta compartida exitosamente con " + usuarioCompartidoCon.getNombre());
        } catch (EntityNotFoundException | NoResultException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al compartir la maqueta: " + e.getMessage());
        }
        return "redirect:/maquetas";
    }

    @GetMapping("/compartidas")
    public String listarMaquetasCompartidas(Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            List<Maqueta> maquetasCompartidas = maquetaService.buscarMaquetasCompartidasConUsuario(usuario.getId());
            model.addAttribute("maquetasCompartidas", maquetasCompartidas);
        }
        return "maquetas_compartidas";
    }

    @GetMapping("/{id}/descargar")
    public ResponseEntity<Resource> descargarMaqueta(@PathVariable Long id) {
        Optional<Maqueta> maquetaOptional = maquetaService.obtenerMaquetaPorId(id);
        if (maquetaOptional.isPresent()) {
            Maqueta maqueta = maquetaOptional.get();
            byte[] archivoAudio = maqueta.getArchivoAudio();
            if (archivoAudio != null) {
                ByteArrayResource resource = new ByteArrayResource(archivoAudio);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + maqueta.getNombre() + ".mp3\"")
                        .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                        .contentLength(archivoAudio.length)
                        .body(resource);
            }
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/eliminar")
    public String eliminarMaqueta(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("Inicio del proceso de eliminación de la maqueta con id: " + id);

            Maqueta maqueta = maquetaService.obtenerMaquetaPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Maqueta no encontrada"));

            System.out.println("Maqueta encontrada: " + maqueta.getNombre());

            Usuario usuario = (Usuario) session.getAttribute("usuario");
            if (usuario == null) {
                System.out.println("Usuario no autenticado");
                throw new NoResultException("No hay usuario autenticado en la sesión");
            }

            System.out.println("Usuario autenticado: " + usuario.getNombre() + ", Rol: " + usuario.getRol());

            if (usuario.getRol() != Rol.ADMINISTRADOR && !usuario.equals(maqueta.getArtista())) {
                System.out.println("El usuario no tiene permisos para eliminar esta maqueta");
                throw new NoResultException("No tienes permiso para eliminar esta maqueta");
            }

            // Eliminar desplegables
            maquetaCompartidaService.eliminarMaquetasCompartidasPorMaqueta(maqueta);
            System.out.println("Entradas de maquetas compartidas eliminadas para la maqueta con id: " + id);

            maquetaService.eliminarMaqueta(id);
            System.out.println("Maqueta eliminada exitosamente: " + maqueta.getNombre());

            redirectAttributes.addFlashAttribute("mensaje", "Maqueta eliminada exitosamente");
        } catch (EntityNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (NoResultException e) {
            System.err.println("Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error inesperado al eliminar la maqueta.");
        }
        return "redirect:/maquetas";
    }
    
    
    @GetMapping("/{id}/comparticiones")
    public String verComparticiones(@PathVariable Long id, Model model) {
        Maqueta maqueta = maquetaService.obtenerMaquetaPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Maqueta no encontrada"));
        List<MaquetaCompartida> comparticiones = maquetaCompartidaService.buscarComparticionesPorMaqueta(maqueta);
        model.addAttribute("comparticiones", comparticiones);
        model.addAttribute("maqueta", maqueta);
        return "ver_comparticiones";
    }

    @PostMapping("/comparticion/{comparticionId}/eliminar")
    public String eliminarComparticion(@PathVariable Long comparticionId, RedirectAttributes redirectAttributes) {
        try {
            maquetaCompartidaService.eliminarComparticion(comparticionId);
            redirectAttributes.addFlashAttribute("mensaje", "Compartición eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la compartición: " + e.getMessage());
        }
        return "redirect:/maquetas";
    }
}