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
        
        if(carrito == null || combo == null) {
        	return null;
        }
        carrito.getCombos().remove(combo); 
        recalcularTotal(carrito);
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
    	if(carrito == null || t == null) {
    		return null;
    	}
    	carrito.getEntradas().remove(t);
    	recalcularTotal(carrito);
    	return carrito;
    }
    
    public void vaciarCarrito(Car carrito) {
        repoCarrito.vaciarCarrito(carrito);
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
