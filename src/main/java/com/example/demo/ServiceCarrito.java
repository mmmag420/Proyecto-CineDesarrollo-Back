package com.example.demo;

import model.Car;
import model.Food;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServiceCarrito {
	
	private final RepositoryCarrito repoCarrito;
    private final RepositoryCombo repoCombo; 

    public ServiceCarrito(RepositoryCarrito repoCarrito, RepositoryCombo repoCombo) {
        this.repoCarrito = repoCarrito;
        this.repoCombo = repoCombo;
    }

    public List<Food> listarCombosDelCarrito() {
        return repoCarrito.obtenerCombos();
    }

    public Car obtenerCarrito() {
        Car c = repoCarrito.obtenerCarrito();
        recalcularTotal(c);
        return c;
    }

    public void agregarComboAlCarrito(int idCombo) {
        Food f = repoCombo.findById(idCombo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Combo no encontrado"));
        repoCarrito.agregarCombo(f);
        Car c = repoCarrito.obtenerCarrito();
        recalcularTotal(c);
        repoCarrito.guardar(c);
    }

    public void eliminarComboDelCarrito(int idCombo) {
        repoCarrito.eliminarComboPorId(idCombo);
        Car c = repoCarrito.obtenerCarrito();
        recalcularTotal(c);
        repoCarrito.guardar(c);
    }

    public void vaciarCarrito() {
        repoCarrito.vaciar();
    }

    private void recalcularTotal(Car c) {
        double total = 0.0;
        for (Food f : c.getCombos()) {
            total += f.getPrecio();
        }
        c.setPrecioFinal(total);
    }

}
