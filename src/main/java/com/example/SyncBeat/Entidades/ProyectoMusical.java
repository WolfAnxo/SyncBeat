package com.example.SyncBeat.Entidades;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "proyectos_musicales")
public class ProyectoMusical {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nombre;

	@Column
	private String descripcion;

	@Column(name = "fecha_inicio")
	private LocalDate fechaInicio;

	@Column(name = "fecha_fin")
	private LocalDate fechaFin;

	@ManyToOne
	@JoinColumn(name = "artista_id")
	private Usuario artista; 

	@ManyToOne
	@JoinColumn(name = "productor_id")
	private Usuario productor; 

	@Column(name = "fecha_lanzamiento")
	private LocalDate fechaLanzamiento;

	@Column
	private String genero;

	@Column
	private BigDecimal presupuesto;

	public ProyectoMusical() {
	}

	public ProyectoMusical(String nombre, Usuario artista, Usuario productor, String genero, String descripcion,
			LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaLanzamiento, BigDecimal presupuesto) {
		this.nombre = nombre;
		this.artista = artista;
		this.productor = productor;
		this.genero = genero;
		this.descripcion = descripcion;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.fechaLanzamiento = fechaLanzamiento;
		this.presupuesto = presupuesto;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Usuario getArtista() {
		return artista;
	}

	public void setArtista(Usuario artista) {
		this.artista = artista;
	}

	public Usuario getProductor() {
		return productor;
	}

	public void setProductor(Usuario productor) {
		this.productor = productor;
	}

	public LocalDate getFechaLanzamiento() {
		return fechaLanzamiento;
	}

	public void setFechaLanzamiento(LocalDate fechaLanzamiento) {
		this.fechaLanzamiento = fechaLanzamiento;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public BigDecimal getPresupuesto() {
		return presupuesto;
	}

	public void setPresupuesto(BigDecimal presupuesto) {
		this.presupuesto = presupuesto;
	}
}