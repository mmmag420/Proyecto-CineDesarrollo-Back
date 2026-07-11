package com.example.demo.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Entity
@Table(
		  name = "salas",
		  uniqueConstraints = @UniqueConstraint(
		    name = "uk_sala_dia_hora",
		    columnNames = {"num_sala","dia_pelicula","hora_inicio"}
		  )
		)
public class Hall {
	
	public enum Dia {
	    LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO
	}
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private int id; 
	
	@Column(name = "num_sala", nullable = false)
	private int numSala;
    
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "movie_id", nullable = false)
	private Movie movie;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "dia_pelicula", nullable = false, length = 15)
	private Dia diaPelicula;
    
    @NotNull
    @Column(name = "hora_inicio", nullable = false)
	private LocalTime horaInicio;
    
    @NotNull
    @Column(name = "hora_fin", nullable = false)
	private LocalTime horaFin;
    
    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private List<Chair> sillas = new ArrayList<>();
	
	public Hall() {}
	
	
	public Hall(int numSala, Movie movie, Dia diaPelicula, LocalTime horaInicio, LocalTime horaFin, List<Chair> sillas) {
		this.numSala = numSala;
		this.movie = movie;
		this.diaPelicula = diaPelicula;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.sillas = (sillas == null) ? new ArrayList<>() : sillas;
	}

	public int getNumSala() {
		return numSala;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	

	public Dia getDiaPelicula() {
		return diaPelicula;
	}

	public void setDiaPelicula(Dia diaPelicula) {
		this.diaPelicula = diaPelicula;
	}

	

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public LocalTime getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(LocalTime horaFin) {
		this.horaFin = horaFin;
	}

	public List<Chair> getSillas() {
		return sillas;
	}

	public void setSillas(List<Chair> sillas) {
		this.sillas = sillas;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}	

}
