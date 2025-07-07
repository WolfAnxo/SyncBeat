package com.example.SyncBeat.Controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CargaController {

	@GetMapping("/")
	public String showLoadingPage() {
		return "pantalla_carga";
	}
}
