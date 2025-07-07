package com.example.SyncBeat.Entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "maquetas_compartidas")
public class MaquetaCompartida {
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Maqueta getMaqueta() {
		return maqueta;
	}

	public void setMaqueta(Maqueta maqueta) {
		this.maqueta = maqueta;
	}

	public Usuario getCompartidoPor() {
		return compartidoPor;
	}

	public void setCompartidoPor(Usuario compartidoPor) {
		this.compartidoPor = compartidoPor;
	}

	public Usuario getCompartidoCon() {
		return compartidoCon;
	}

	public void setCompartidoCon(Usuario compartidoCon) {
		this.compartidoCon = compartidoCon;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "maqueta_id")
	private Maqueta maqueta;

	@ManyToOne
	@JoinColumn(name = "compartido_por_id")
	private Usuario compartidoPor;

	@ManyToOne
	@JoinColumn(name = "compartido_con_id")
	private Usuario compartidoCon;
}
