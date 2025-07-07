package com.example.SyncBeat.Entidades;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nombre;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String contrasena;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Rol rol;

	@OneToMany(mappedBy = "artista", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProyectoMusical> proyectosMusicalesArtista = new ArrayList<>();

	@OneToMany(mappedBy = "productor", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProyectoMusical> proyectosMusicalesProductor = new ArrayList<>();

	public Usuario() {
	}

	public Usuario(String nombre, String email, String contrasena, Rol rol) {
		this.nombre = nombre;
		this.email = email;
		this.contrasena = contrasena;
		this.rol = rol;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) { // Corrección aquí
		this.contrasena = contrasena;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public List<ProyectoMusical> getProyectosMusicalesArtista() {
		return proyectosMusicalesArtista;
	}

	public void setProyectosMusicalesArtista(List<ProyectoMusical> proyectosMusicalesArtista) {
		this.proyectosMusicalesArtista = proyectosMusicalesArtista;
	}

	public List<ProyectoMusical> getProyectosMusicalesProductor() {
		return proyectosMusicalesProductor;
	}

	public void setProyectosMusicalesProductor(List<ProyectoMusical> proyectosMusicalesProductor) {
		this.proyectosMusicalesProductor = proyectosMusicalesProductor;
	}


	//Metodo equals compara objectos, compara usuario si apunta al mismo usuario
	@Override
	public boolean equals(Object o) {
		if (this == o) //Si this es igual al objecto es verdadero
			return true;
		if (o == null || getClass() != o.getClass())
			return false;//Si el objecto es nulo o no es igual a lo que se apunta a la clase salta false
		Usuario usuario = (Usuario) o; //Se compara el objecto usuario con el objecto o y si no es nulo y es igual retorna true
		return id != null && id.equals(usuario.id);
	}

	@Override
	public int hashCode() { //Cuando se establece la igualdad por equals, se les otorga un hash
		return 31;
	}
}
