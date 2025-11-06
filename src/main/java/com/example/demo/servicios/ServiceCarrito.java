package com.example.demo.servicios;



import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.Bill;
import com.example.demo.model.Car;
import com.example.demo.model.Chair;
import com.example.demo.model.Food;
import com.example.demo.model.Hall;
import com.example.demo.model.Ticket;
import com.example.demo.repositorios.RepositoryCarrito;
import com.example.demo.repositorios.RepositoryCombo;

import jakarta.transaction.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
public class ServiceCarrito {
	
	private static final int PRECIO_ENTRADA = 14000;
    private static final AtomicInteger SEQ_ENTRADA = new AtomicInteger(1);
	
	private final RepositoryCarrito repoCarrito;
    private final RepositoryCombo repoCombo; 
    

    public ServiceCarrito(RepositoryCarrito repoCarrito, RepositoryCombo repoCombo) {
        this.repoCarrito = repoCarrito;
        this.repoCombo = repoCombo;
    }
        
    public boolean agregarCarritoEnFactura(Car carrito, Bill factura) {
        if (carrito == null || factura == null) return false;
        if (factura.getCarrito() != null) return false; 

        factura.setCarrito(carrito);
        return true;
      }
    
    
 
    //agrega un combo al carrito y actualiza el precio
	public Car agregarComboAlCarrito(Car carrito, Food combo) {
		if(carrito == null || combo == null) {
			return null;
		}
		
		Food comboBD = repoCombo.findById(combo.getIdCombo()).orElse(null);
		if (comboBD == null) return carrito;
		
		carrito.getCombos().add(comboBD);
		recalcularTotal(carrito);
		return repoCarrito.save(carrito);
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
                return repoCarrito.save(carrito);
            }
        }

        return repoCarrito.save(carrito);
    }
    
    //agrega entradas al carrito y calcula su precio
    public Car agregarEntradaAlCarrito(Car carrito, Ticket t) {
        if (carrito == null || t == null || t.getSala() == null) {
            return null;
        }    	
        
    	if(t.getNumEntrada() <= 0 || t.getSala().getNumSala() <= 0) {
    		return null;
    	}
    	  	
    	carrito.getEntradas().add(t);
    	recalcularTotal(carrito);
    	return repoCarrito.save(carrito);
    }
    
    
    public Car eliminarEntradaDelCarrito(Car carrito, Ticket t) {
        if (carrito == null || t == null) return null;

        Integer numEntrada = t.getNumEntrada();
        Hall funcion = t.getSala();

        List<Ticket> entradas = carrito.getEntradas();
        if (entradas == null || entradas.isEmpty()) return carrito;

        for (Iterator<Ticket> it = entradas.iterator(); it.hasNext();) {
            Ticket cur = it.next();
            if (cur != null
                    && java.util.Objects.equals(cur.getNumEntrada(), numEntrada)
                    && mismaFuncion(cur.getSala(), funcion)) {
                it.remove();
                recalcularTotal(carrito);
                return repoCarrito.save(carrito);
            }
        }
        return repoCarrito.save(carrito);
    }
    
    private boolean mismaFuncion(Hall a, Hall b) {
        if (a == null || b == null) return false;
        return a.getNumSala() == b.getNumSala()
            && a.getDiaPelicula() == b.getDiaPelicula()
            && java.util.Objects.equals(a.getHoraInicio(), b.getHoraInicio());
    }
    
    public Car vaciarCarrito(Car carrito) {
    	if (carrito == null) return null;
    	carrito.getCombos().clear();
    	carrito.getEntradas().clear();
        recalcularTotal(carrito);
        return repoCarrito.save(carrito);
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
        return carrito != null ? carrito.getCombos() : List.of();
    }    
    
    public List<Ticket> listarEntradasDelCarrito(Car carrito) {
    	return carrito != null ? carrito.getEntradas() : List.of();
    }

}
