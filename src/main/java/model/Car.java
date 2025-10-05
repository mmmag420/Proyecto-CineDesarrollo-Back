package model;

import java.util.ArrayList;

public class Car {
	
	private int idCarrito;
	private ArrayList<Ticket> entradas;
	private ArrayList<Food> combos;
	private String metodoDePago;
	private boolean estado;
	private double precioFinal;
	
	public Car(int idCarrito, ArrayList<Ticket> entradas, ArrayList<Food> combos, String metodoDePago, boolean estado, double precioFinal) {
		this.idCarrito = idCarrito;
		this.entradas = entradas;
		this.combos = combos;
		this.metodoDePago = metodoDePago;
		this.estado = estado;
		this.precioFinal = precioFinal;
	}

	public int getIdCarrito() {
		return idCarrito;
	}

	public ArrayList<Ticket> getEntradas() {
		return entradas;
	}

	public void setEntradas(ArrayList<Ticket> entradas) {
		this.entradas = entradas;
	}

	public ArrayList<Food> getCombos() {
		return combos;
	}

	public void setCombos(ArrayList<Food> combos) {
		this.combos = combos;
	}

	public String getMetodoDePago() {
		return metodoDePago;
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
