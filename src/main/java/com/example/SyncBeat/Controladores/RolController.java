package com.example.SyncBeat.Controladores;

import com.example.SyncBeat.Entidades.Rol;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/roles")
public class RolController {

    @GetMapping
    public List<Rol> listarRoles() {
        return Arrays.asList(Rol.values());
    }
}
