package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "entradas")
public class Ticket {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;  
	
	@Column(name = "num_entrada", nullable = false)
	private int numEntrada;
	
	@Column(name = "precio_entrada", nullable = false)
	private double precioEntrada;
    

    @ManyToOne(optional = false)
    @JoinColumn(name = "sala_id", nullable = false)
	private Hall sala;
    
    @Column(name="movie_titulo_compra")
    private String movieTituloCompra;
    
    @Column(name="movie_id_compra")
    private String movieIdCompra;
	
	public Ticket() {}
	
	public Ticket(int numEntrada, int precioEntrada, Hall sala) {
		this.numEntrada = numEntrada;
		this.precioEntrada = precioEntrada;
		this.sala = sala;
	}

	public int getNumEntrada() {
		return numEntrada;
	}

	public void setNumEntrada(int numEntrada) {
		this.numEntrada = numEntrada;
	}

	public double getPrecioEntrada() {
		return precioEntrada;
	}

	public void setPrecioEntrada(double precioEntrada) {
		this.precioEntrada = precioEntrada;
	}

	public Hall getSala() {
		return sala;
	}

	public void setSala(Hall sala) {
		this.sala = sala;
	}

	public String getMovieTituloCompra() {
		return movieTituloCompra;
	}

	public void setMovieTituloCompra(String movieTituloCompra) {
		this.movieTituloCompra = movieTituloCompra;
	}

	public String getMovieIdCompra() {
		return movieIdCompra;
	}

	public void setMovieIdCompra(String movieIdCompra) {
		this.movieIdCompra = movieIdCompra;
	}
	
	

}
