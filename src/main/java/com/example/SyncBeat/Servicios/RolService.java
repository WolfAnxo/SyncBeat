package com.example.SyncBeat.Servicios;

import org.springframework.stereotype.Service;

import com.example.SyncBeat.Entidades.Rol;

import java.util.Arrays;
import java.util.List;

@Service
public class RolService {

	public List<Rol> listarRoles() {
		return Arrays.asList(Rol.values());
	}

	public Rol obtenerRolPorNombre(String nombre) {
		return Rol.valueOf(nombre);
	}
}
