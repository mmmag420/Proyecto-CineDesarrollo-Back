package com.example.demo;

import model.Bill;
import model.Car;
import model.Food;
import model.Ticket;
import model.Hall;
import model.Chair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ServiceCarrito {
	
	private static final int PRECIO_ENTRADA = 14000;
    private static final AtomicInteger SEQ_ENTRADA = new AtomicInteger(1);
	
	private final RepositoryCarrito repoCarrito;
    private final RepositoryCombo repoCombo; 

    public ServiceCarrito(RepositoryCarrito repoCarrito, RepositoryCombo repoCombo) {
        this.repoCarrito = repoCarrito;
        this.repoCombo = repoCombo;
    }
    
    //guarda el carrito en su respectiva clase factura
    public boolean agregarCarritoEnFactura(Car carrito, Bill factura) {
    	if(carrito == null || factura == null) return false;
    	if(factura.getCarrito() != null) {
    		return false;
    	}
    	return repoCarrito.guardarCarroEnFactura(factura, carrito);
    }
    
    
 
    //agrega un combo al carrito y actualiza el precio
	public Car agregarComboAlCarrito(Car carrito, Food combo) {
		if(carrito == null || combo == null) {
			return null;
		}
		repoCarrito.agregarComboAlCarrito(carrito, combo);
		recalcularTotal(carrito);
		return carrito;
	}
	
	//quita una unidad de un combo en el carrito
    public Car quitarUnaUnidadCombo(Car carrito, Food combo) {
        if (carrito == null || combo == null) return null;

        Integer id = combo.getIdCombo();
        List<Food> combos = carrito.getCombos();
        if (combos == null || combos.isEmpty()) return carrito;

        for (Iterator<Food> it = combos.iterator(); it.hasNext();) {
            Food c = it.next();
            if (c != null && java.util.Objects.equals(c.getIdCombo(), id)) {
                it.remove();             
                recalcularTotal(carrito);   
                return carrito;
            }
        }

        return carrito;
    }
    
    //agrega entradas al carrito y calcula su precio
    public Car agregarEntradaAlCarrito(Car carrito, Ticket t) {
        if (carrito == null || t == null || t.getSala() == null) {
            return null;
        }    	
        
    	if(t.getNumEntrada() <= 0 || t.getSala().getNumSala() <= 0) {
    		return null;
    	}
    	  	
    	repoCarrito.agregarEntradasAlCarrito(carrito, t);
    	recalcularTotal(carrito);
    	return carrito;
    }
    
    
    public Car eliminarEntradaDelCarrito(Car carrito, Ticket t) {
        if (carrito == null || t == null) return null;

        Integer numEntrada = t.getNumEntrada();
        List<Ticket> entradas = carrito.getEntradas();
        if (entradas == null || entradas.isEmpty()) return carrito;

        for (Iterator<Ticket> it = entradas.iterator(); it.hasNext();) {
            Ticket ticket = it.next();
            if (t != null && java.util.Objects.equals(t.getNumEntrada(), numEntrada)) {
                it.remove();                 // ❗ Elimina solo la primera coincidencia
                recalcularTotal(carrito);    // Actualiza el total
                return carrito;
            }
        }

        return carrito; // No encontró coincidencia
    }



    /*private void recalcularYGuardar() {
        Car c = repoCarrito.obtenerCarrito();
        recalcularTotal(c);
        repoCarrito.guardar(c);
    }*/

    private void recalcularTotal(Car carrito) {
        double total = 0.0;
        for (Food f : carrito.getCombos()) total += f.getPrecio();
        for (Ticket t : carrito.getEntradas()) total += t.getPrecioEntrada();
        carrito.setPrecioFinal(total);
    }
    
    public List<Food> listarCombosDelCarrito(Car carrito) {
        return repoCarrito.obtenerCombosDelCarrito(carrito);
    }    
    
    public List<Ticket> listarEntradasDelCarrito(Car carrito) {
        return repoCarrito.obtenerEntradasDelCarrito(carrito);
    }

}
