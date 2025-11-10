package com.example.demo.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.repositorios.RepositoryCarrito;
import com.example.demo.repositorios.RepositoryClient;

import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Bill;
import com.example.demo.model.Car;
import com.example.demo.model.Client;
import com.example.demo.model.Hall;
import com.example.demo.model.Ticket;

@Service
@Transactional
public class ServiceClient {

	private final RepositoryClient repoClient;
	private final RepositoryCarrito repoCar;

	public ServiceClient(RepositoryClient repoClient, RepositoryCarrito repoCar) {
		this.repoClient = repoClient;
		this.repoCar = repoCar;
	}
	
	public List<Client> listarClientes() {
		return repoClient.findAll();
	}

	
	public boolean guardarCliente(Client cliente) {
		
		if (repoClient.existsByCedula(cliente.getCedula())) return false;	
		
		if(cliente.getEdad() < 18) {
			return false;
		}	
		
		repoClient.save(cliente);
		return true;
	}
	
	public Client buscarCliente(String cedula) {
		return repoClient.findByCedula(cedula).orElse(null);
	}
	
	public boolean editarCliente(Client cliente) {
		if (cliente.getEdad() < 18) return false;
		
        Optional<Client> actualOpt = repoClient.findByCedula(cliente.getCedula());
        if (actualOpt.isEmpty()) return false;
		
        Client actual = actualOpt.get();
        actual.setEstadoMembresia(cliente.isEstadoMembresia());


        repoClient.save(actual);
        return true;
		
	}
	
	public boolean eliminarCliente(Client cliente) {
        Optional<Client> actual = repoClient.findByCedula(cliente.getCedula());
        if (actual.isEmpty()) return false;
        repoClient.delete(actual.get());
        return true;
	}
	
    public Client buscarPorCorreoYContraseña(String correo, String contraseña) {
        return repoClient.findByCorreoAndContrasena(correo, contraseña).orElse(null);
    }
    
    @Transactional(readOnly = true)
    public ArrayList<Bill> obtenerTodasLasFacturasDelUser(Client cliente) {
    	  Client c = buscarCliente(cliente.getCedula());
    	    if (c == null) return new ArrayList<>();

    	    var facturas = c.getHistorial();
    	    if (facturas == null || facturas.isEmpty()) return new ArrayList<>();

    	    // Forzar carga de asociaciones LAZY antes de serializar en el front
    	    for (Bill f : facturas) {
    	        Car car = f.getCarrito();
    	        if (car != null) {
    	            if (car.getCombos() != null)   car.getCombos().size();
    	            if (car.getEntradas() != null) {
    	                car.getEntradas().size();
    	                for (Ticket t : car.getEntradas()) {
    	                    if (t != null && t.getSala() != null) {
    	                        Hall h = t.getSala();
    	                        h.getNumSala(); // toca algo simple
    	                        if (h.getMovie() != null) h.getMovie().getNombre();
    	                    }
    	                }
    	            }
    	        }
    	    }

    	    return new ArrayList<>(facturas);
    }
	
    @Transactional
    public boolean agregarFacturaClient(Client cliente, Bill factura) {
        Client c = buscarCliente(cliente.getCedula());
        if (c == null) return false;

        if (factura.getCedulaCliente() == null || factura.getCedulaCliente().isBlank()) {
            factura.setCedulaCliente(c.getCedula());
        }

        if (factura.getCarrito() == null || factura.getCarrito().getIdCarrito() <= 0) {
            return false;
        }

        Car managedCar = repoCar.findById(factura.getCarrito().getIdCarrito()).orElse(null);
        if (managedCar == null) return false;


        managedCar.setEstado(true);
        factura.setCarrito(managedCar);
        factura.setCliente(c);

        if (c.getHistorial() == null) c.setHistorial(new ArrayList<>());
        c.getHistorial().add(factura);

        repoClient.save(c);
        return true;
    }
    
 
	
}
