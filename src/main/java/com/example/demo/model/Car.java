package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "carritos")
public class Car {
	
	@Id	
	@Column(name = "id_carrito")
	private int idCarrito;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "carrito_id")
	private List<Ticket> entradas = new ArrayList<>();
	
	@ManyToMany
	@JoinTable(
	  name = "carrito_combos",
	  joinColumns = @JoinColumn(name = "carrito_id"),
	  inverseJoinColumns = @JoinColumn(name = "combo_id", referencedColumnName = "id_combo")
	)
	private List<Food> combos = new ArrayList<>();
	
		
	@Column(nullable = false)
	private boolean estado;
	
	@Column(name = "precio_final", nullable = false)
	private double precioFinal;
	
	public Car() {	
	}
	
	public Car(int idCarrito, List<Ticket> entradas, List<Food> combos, boolean estado, double precioFinal) {
		this.idCarrito = idCarrito;
		this.entradas = (entradas == null) ? new ArrayList<Ticket>() : entradas;
		this.combos = (combos == null) ? new ArrayList<Food>() : combos;
		this.estado = estado;
		this.precioFinal = precioFinal;
	}

	public int getIdCarrito() {
		return idCarrito;
	}

	public void setIdCarrito(int idCarrito) {
		this.idCarrito = idCarrito;
	}

	public List<Ticket> getEntradas() {
		return entradas;
	}

	public void setEntradas(List<Ticket> entradas) {
		this.entradas = entradas;
	}

	public List<Food> getCombos() {
		return combos;
	}

	public void setCombos(List<Food> combos) {
		this.combos = combos;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public double getPrecioFinal() {
		return precioFinal;
	}

	public void setPrecioFinal(double precioFinal) {
		this.precioFinal = precioFinal;
	}
	
}
