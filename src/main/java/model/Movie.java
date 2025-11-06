package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "peliculas")
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
    
    @Size(max = 10)
    @Column(length = 10)
    private String clasificacion;
    
    @Size(max = 500)
    @Column(length = 500)
    private String reparto;
    
    @Size(max = 120)
    @Column(length = 120)
    private String director;
    
    @Size(max = 255)
    @Column(length = 255)
    private String rutaImagen;
    
    @Size(max = 255)
    @Column(length = 255)
    private String rutaImagenBoton;
    
    @Size(max = 255)
    @Column(length = 255)
    private String trailer;
    
    @Size(max = 20)
    @Column(length = 20)
    private String duracion;
    
    public Movie() {}
    
    public Movie(String id, String nombre, String descripcion, String clasificacion, String reparto, String director, String rutaImagen, String rutaImagenBoton, String trailer, String duracion) {
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

    public String getDescripcion() {
        return descripcion;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public String getReparto() {
        return reparto;
    }

    public String getDirector() {
        return director;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public String getTrailer() {
        return trailer;
    }

    public String getDuracion() {
        return duracion;
    }

	public String getRutaImagenBoton() {
		return rutaImagenBoton;
	}

	public void setRutaImagenBoton(String rutaImagenBoton) {
		this.rutaImagenBoton = rutaImagenBoton;
	}
    
    
    
    
}
