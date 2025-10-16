package com.example.demo;

import model.Bill;
import model.Car;
import model.Food;
import model.Ticket;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;


@Repository
public class RepositoryCarrito {
	

	public RepositoryCarrito() {
		
	}
	
	public boolean guardarCarroEnFactura(Bill factura, Car carrito) {		
		factura.setCarrito(carrito);
		return true;
	}
	
	public void vaciarCarrito(Car carrito) {
        carrito.getCombos().clear();
        carrito.getEntradas().clear();
        carrito.setPrecioFinal(0.0);
	}
	
	public boolean agregarComboAlCarrito(Car carrito, Food combo) {
		carrito.getCombos().add(combo);
		return true;
	}
	
	public List<Food> obtenerCombosDelCarrito(Car carrito) {
		return new ArrayList<>(carrito.getCombos());
	}
	
	public boolean agregarEntradasAlCarrito(Car carrito, Ticket t) {
		carrito.getEntradas().add(t);
		return true;
	}
	
	public List<Ticket> obtenerEntradasDelCarrito(Car carrito) {
		return new ArrayList<>(carrito.getEntradas());
	}
	
	
	

}
