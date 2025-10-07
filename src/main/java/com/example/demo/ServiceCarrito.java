package com.example.demo;

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

    public List<Food> listarCombosDelCarrito() {
        return repoCarrito.obtenerCombos();
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
    
    public List<Ticket> listarEntradasDelCarrito() {
        return repoCarrito.obtenerEntradas();
    }

    public void agregarEntradaAlCarrito(int numSala, int numSilla) {
        if (numSala <= 0 || numSilla <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sala o silla inválida");
        }

        Chair sillaSeleccionada = new Chair(numSilla, true);
        Chair[] sillas = new Chair[]{ sillaSeleccionada };
        Hall hallMinimo = new Hall(numSala, null, "", "", "", sillas);
        
        Ticket t = new Ticket(SEQ_ENTRADA.getAndIncrement(), PRECIO_ENTRADA, hallMinimo);

        repoCarrito.agregarEntrada(t);
        recalcularYGuardar();
    }

    public void eliminarEntradaDelCarrito(int numEntrada) {
        repoCarrito.eliminarEntradaPorNumero(numEntrada);
        recalcularYGuardar();
    }

    public Car obtenerCarrito() {
        Car c = repoCarrito.obtenerCarrito();
        recalcularTotal(c);
        return c;
    }

    public void vaciarCarrito() {
        repoCarrito.vaciar();
    }

    private void recalcularYGuardar() {
        Car c = repoCarrito.obtenerCarrito();
        recalcularTotal(c);
        repoCarrito.guardar(c);
    }

    private void recalcularTotal(Car c) {
        double total = 0.0;
        for (Food f : c.getCombos()) total += f.getPrecio();
        for (Ticket t : c.getEntradas()) total += t.getPrecioEntrada();
        c.setPrecioFinal(total);
    }

}
