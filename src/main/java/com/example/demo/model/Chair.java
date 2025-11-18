package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(
	    name = "sillas",
	    uniqueConstraints = @UniqueConstraint(
	        name = "uk_sala_num_silla",
	        columnNames = {"sala_id", "num_silla"}
	    )
	)
public class Chair {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
    @Column(name = "num_silla", nullable = false)
	private int numSilla;
    
    @NotNull
    @Column(nullable = false)
	private boolean estado;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sala_id", nullable = false)
    @JsonIgnore 
    private Hall sala;
	
	public Chair() {}
	
	public Chair(int numSilla, boolean estado, Hall sala) {
		this.numSilla = numSilla;
		this.estado = estado;
		this.sala = sala;		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	

	public Hall getSala() {
		return sala;
	}

	public void setSala(Hall sala) {
		this.sala = sala;
	}

	public void setNumSilla(int numSilla) {
		this.numSilla = numSilla;
	}

	public int getNumSilla() {
		return numSilla;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}	
	
	
}
