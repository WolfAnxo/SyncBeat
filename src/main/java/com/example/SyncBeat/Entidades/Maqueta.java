package com.example.SyncBeat.Entidades;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.*;

@Entity
@Table(name = "maquetas")
public class Maqueta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nombre;

	@Lob
	@Column(name = "archivo_audio", columnDefinition = "LONGBLOB")
	private byte[] archivoAudio;

	@ManyToOne
	@JoinColumn(name = "artista_id")
	private Usuario artista;

	@ManyToOne
	@JoinColumn(name = "productor_id")
	private Usuario productor;

	@OneToMany(mappedBy = "maqueta", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MaquetaCompartida> maquetasCompartidas = new ArrayList<>();

	@Transient
	private MultipartFile archivo;

	public Maqueta() {
	}

	public Maqueta(String nombre) {
		this.nombre = nombre;
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

	public byte[] getArchivoAudio() {
		return archivoAudio;
	}

	public void setArchivoAudio(byte[] archivoAudio) {
		this.archivoAudio = archivoAudio;
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

	public List<MaquetaCompartida> getMaquetasCompartidas() {
		return maquetasCompartidas;
	}

	public void setMaquetasCompartidas(List<MaquetaCompartida> maquetasCompartidas) {
		this.maquetasCompartidas = maquetasCompartidas;
	}

	public MultipartFile getArchivo() {
		return archivo;
	}

	public void setArchivo(MultipartFile archivo) {
		this.archivo = archivo;
	}
}