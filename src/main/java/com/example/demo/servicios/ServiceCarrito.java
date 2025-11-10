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
import com.example.demo.repositorios.RepositorySala;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
public class ServiceCarrito {
	
	private static final int PRECIO_ENTRADA = 14000;
    private static final AtomicInteger SEQ_ENTRADA = new AtomicInteger(1);
	
	private final RepositoryCarrito repoCarrito;
    private final RepositoryCombo repoCombo; 
    private final RepositorySala repoSala;
    

    public ServiceCarrito(RepositoryCarrito repoCarrito, RepositoryCombo repoCombo, RepositorySala repoSala) {
        this.repoCarrito = repoCarrito;
        this.repoCombo = repoCombo;
        this.repoSala = repoSala;
    }
        
    public boolean agregarCarritoEnFactura(Car carrito, Bill factura) {
        if (carrito == null || factura == null) return false;
        if (factura.getCarrito() != null) return false; 

        factura.setCarrito(carrito);
        return true;
      }
    
    
 
    //agrega un combo al carrito y actualiza el precio
    @Transactional
    public Car agregarComboAlCarrito(Car carrito, Food combo) {
        if (carrito == null || combo == null) return null;
        if (carrito.getIdCarrito() <= 0) return null;

        // 1) Reatachar o crear el carrito "managed"
        Car managed = repoCarrito.findById(carrito.getIdCarrito()).orElse(null);
        if (managed == null) {
            managed = new Car();
            managed.setIdCarrito(carrito.getIdCarrito());
            managed.setEstado(false);
            managed.setPrecioFinal(0.0);
            managed.setEntradas(new ArrayList<>());
            managed.setCombos(new ArrayList<>());
            managed = repoCarrito.save(managed);
        }

        // 2) Asegurar listas no nulas
        if (managed.getCombos() == null)   managed.setCombos(new ArrayList<>());
        if (managed.getEntradas() == null) managed.setEntradas(new ArrayList<>());

        // 3) Resolver el combo REAL desde BD (no uses el del body)
        Food real = repoCombo.findById(combo.getIdCombo()).orElse(null);
        if (real == null) return null;

        // 4) Evitar duplicado exacto (opcional)
        boolean yaEsta = managed.getCombos().stream()
            .anyMatch(c -> Objects.equals(c.getIdCombo(), real.getIdCombo()));
        if (!yaEsta) {
            managed.getCombos().add(real);
        }

        // 5) Recalcular y guardar SOLO el managed
        recalcularTotal(managed);
        return repoCarrito.save(managed);
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
    @Transactional
    public Car agregarEntradaAlCarrito(Car carrito, Ticket t) {
        if (carrito == null || t == null || t.getSala() == null) return null;

        // Re-attach o crea
        if (carrito.getIdCarrito() <= 0) return null;
        Car managed = repoCarrito.findById(carrito.getIdCarrito()).orElse(null);
        if (managed == null) {
            managed = new Car();
            managed.setIdCarrito(carrito.getIdCarrito());
            managed.setEstado(false);
            managed.setPrecioFinal(0.0);
            managed.setEntradas(new java.util.ArrayList<>());
            managed.setCombos(new java.util.ArrayList<>());
            managed = repoCarrito.save(managed);
        }

        if (managed.getEntradas() == null) managed.setEntradas(new java.util.ArrayList<>());
        if (managed.getCombos()   == null) managed.setCombos(new java.util.ArrayList<>());

        // Hall real (con id)
        Hall s = t.getSala();
        Hall salaReal = repoSala.findByNumSalaAndDiaPeliculaAndHoraInicio(
                s.getNumSala(), s.getDiaPelicula(), s.getHoraInicio()
        ).orElse(null);
        if (salaReal == null) return null;

        // Ticket listo para insert
        t.setSala(salaReal);
        if (t.getPrecioEntrada() <= 0) t.setPrecioEntrada(14000);
        // si tienes getId/setId(int):
        // if (t.getId() != 0) t.setId(0);

        // Evitar duplicado
        boolean yaExiste = managed.getEntradas().stream()
            .anyMatch(e -> e.getSala() != null
                        && java.util.Objects.equals(e.getSala().getId(), salaReal.getId())
                        && e.getNumEntrada() == t.getNumEntrada());
        if (yaExiste) return managed;

        managed.getEntradas().add(t);
        recalcularTotal(managed);

        return repoCarrito.save(managed);
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
    
    @Transactional
    public Car vaciarCarrito(Car carrito) {
        if (carrito == null || carrito.getIdCarrito() <= 0) return null;

        Car managed = repoCarrito.findById(carrito.getIdCarrito()).orElse(null);
        if (managed == null) return null;

        // 🔒 Si este carrito ya se usó en una factura, no lo vaciamos
        if (managed.isEstado()) {
            return null; // indica al front que debe crear un carrito nuevo
        }

        // Vaciar SOLO si no está facturado
        if (managed.getEntradas() != null) managed.getEntradas().clear();
        if (managed.getCombos()   != null) managed.getCombos().clear();

        managed.setPrecioFinal(0.0);
        managed.setEstado(false);

        return repoCarrito.save(managed);
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
