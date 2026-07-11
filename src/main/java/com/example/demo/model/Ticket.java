package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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

}
