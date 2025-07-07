package com.example.SyncBeat.Entidades;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "eventos")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    @Column
    private String lugar;

    @Column
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "artista_id")
    private Usuario artista;

    public Evento() {
    }

    public Evento(String nombre, LocalDateTime fechaHora, String lugar, String descripcion, Usuario artista) {
        this.nombre = nombre;
        this.fechaHora = fechaHora;
        this.lugar = lugar;
        this.descripcion = descripcion;
        this.artista = artista;
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

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuario getArtista() {
        return artista;
    }

    public void setArtista(Usuario artista) {
        this.artista = artista;
    }
}