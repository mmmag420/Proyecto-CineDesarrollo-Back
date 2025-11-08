package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "peliculas")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Movie {
	
    @Id
    @Column(name = "id", nullable = false, length = 64)
	private String id;
    
    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String nombre;
    
    @NotBlank
    @Lob    
    private String descripcion;
    
    @Column(name = "clasificacion", length = 250)
    private String clasificacion;
    
    @Column(name = "reparto", length = 300)
    private String reparto;
    
    @Column(name = "director", length = 200)
    private String director;
    
    @Column(name = "rutaImagen", length = 300)
    private String rutaImagen;
    
    @Column(name = "rutaImagenBoton", length = 300)
    private String rutaImagenBoton;
    
    @Column(name = "trailer", length = 1000)
    private String trailer;
    
    @Size(max = 20)
    @Column(length = 20)
    private String duracion;
    
    @NotNull
    @Column(nullable = false)
    private boolean estado;
    

    public Movie() {}
    
    public Movie(String id, String nombre, String descripcion, String clasificacion, String reparto, String director, String rutaImagen, String rutaImagenBoton, String trailer, String duracion, boolean estado) {
    	this.id = id;;
    	this.nombre = nombre;
        this.descripcion = descripcion;
        this.clasificacion = clasificacion;
        this.reparto = reparto;
        this.director = director;
        this.rutaImagen = rutaImagen;
        this.rutaImagenBoton = rutaImagenBoton;
        this.trailer = trailer;
        this.duracion = duracion;
        this.estado = estado;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public String getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}

	public String getReparto() {
		return reparto;
	}

	public void setReparto(String reparto) {
		this.reparto = reparto;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getRutaImagen() {
		return rutaImagen;
	}

	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}

	public String getRutaImagenBoton() {
		return rutaImagenBoton;
	}

	public void setRutaImagenBoton(String rutaImagenBoton) {
		this.rutaImagenBoton = rutaImagenBoton;
	}

	public String getTrailer() {
		return trailer;
	}

	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}

	public String getDuracion() {
		return duracion;
	}

	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	
    
	
	
	
    
    
    
    
}
