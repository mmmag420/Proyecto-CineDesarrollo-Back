package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "sillas")
public class Chair {
	
    @Id
    @Column(name = "num_silla", nullable = false)
	private int numSilla;
    
    @NotNull
    @Column(nullable = false)
	private boolean estado;
	
	//private Hall sala; quitarla si estoy seguro de que esa es la relacion de chair sala y sala chair, aunque no cambie de vector a list en la clase sala
	
	public Chair() {}
	
	public Chair(int numSilla, boolean estado) {
		this.numSilla = numSilla;
		this.estado = estado;
		//this.sala = sala;
		
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
