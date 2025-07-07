package com.example.SyncBeat.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.SyncBeat.Entidades.ProyectoMusical;
import com.example.SyncBeat.Servicios.ProyectoMusicalService;

import java.util.List;

@Controller
@RequestMapping("/proyectos-musicales")
public class ProyectoMusicalController {

    @Autowired
    private ProyectoMusicalService proyectoMusicalService;

    @GetMapping
    public List<ProyectoMusical> listarProyectosMusicales() {
        return proyectoMusicalService.listarProyectosMusicales();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProyectoMusical> obtenerProyectoMusicalPorId(@PathVariable Long id) {
        return proyectoMusicalService.obtenerProyectoMusicalPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProyectoMusical> crearProyectoMusical(@RequestBody ProyectoMusical proyectoMusical) {
        ProyectoMusical nuevoProyecto = proyectoMusicalService.guardarProyectoMusical(proyectoMusical);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProyecto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProyectoMusical> actualizarProyectoMusical(@PathVariable Long id, @RequestBody ProyectoMusical proyectoMusical) {
        if (!proyectoMusicalService.obtenerProyectoMusicalPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        proyectoMusical.setId(id);
        ProyectoMusical proyectoActualizado = proyectoMusicalService.guardarProyectoMusical(proyectoMusical);
        return ResponseEntity.ok(proyectoActualizado);
    }

    @PostMapping("/{id}/eliminar")
    public ResponseEntity<Void> eliminarProyectoMusical(@PathVariable Long id) {
        if (!proyectoMusicalService.obtenerProyectoMusicalPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        proyectoMusicalService.eliminarProyectoMusical(id);
        return ResponseEntity.noContent().build();
    }
    
    
    
}