package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "combos")
public class Food {

	@Id
	@Column(name = "id_combo")
	private int idCombo;
    
    @NotNull
    @Column(name = "precio", nullable = false)
	private double precio;
    
    @NotBlank
    @Column(nullable = false, length = 255)
	private String descripcion;
	
	public Food() {}
	
	public Food(int idCombo, double precio, String descripcion) {
		this.idCombo = idCombo;
		this.precio = precio;
		this.descripcion = descripcion;
	}

	public int getIdCombo() {
		return idCombo;
	}

	public void setIdCombo(int idCombo) {
		this.idCombo = idCombo;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
