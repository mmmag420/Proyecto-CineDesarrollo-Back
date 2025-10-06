package com.example.demo;

import model.Car;
import model.Food;
import model.Ticket;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class RepositoryCarrito {

	    private final Car carrito = new Car(
	            1,
	            new ArrayList<Ticket>(),
	            new ArrayList<Food>(),
	            true,
	            0.0
	    );

	    public List<Food> obtenerCombos() {
	        return new ArrayList<>(carrito.getCombos());
	    }

	    public void agregarCombo(Food food) {
	        carrito.getCombos().add(food);
	    }

	    public void eliminarComboPorId(int idCombo) {
	        carrito.getCombos().removeIf(f -> f.getIdCombo() == idCombo);
	    }
	    
	    public List<Ticket> obtenerEntradas() {
	        return new ArrayList<>(carrito.getEntradas());
	    }

	    public void agregarEntrada(Ticket t) {
	        carrito.getEntradas().add(t);
	    }

	    public void eliminarEntradaPorNumero(int numEntrada) {
	        carrito.getEntradas().removeIf(t -> t.getNumEntrada() == numEntrada);
	    }

	    public void vaciar() {
	        carrito.getCombos().clear();
	        carrito.getEntradas().clear();
	        carrito.setPrecioFinal(0.0);
	    }

	    public Car obtenerCarrito() {
	        return carrito;
	    }

	    public void guardar(Car car) {
	        carrito.setPrecioFinal(car.getPrecioFinal());
	    }

}
