package com.example.demo.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.repositorios.RepositoryClient;

import jakarta.transaction.Transactional;

import com.example.demo.model.Bill;
import com.example.demo.model.Client;

@Service
@Transactional
public class ServiceClient {

	private final RepositoryClient repo;

	public ServiceClient(RepositoryClient repo) {
		this.repo = repo;
	}
	
	public List<Client> listarClientes() {
		return repo.findAll();
	}

	
	public boolean guardarCliente(Client cliente) {
		
		if (repo.existsByCedula(cliente.getCedula())) return false;	
		
		if(cliente.getEdad() < 18) {
			return false;
		}	
		
		repo.save(cliente);
		return true;
	}
	
	public Client buscarCliente(String cedula) {
		return repo.findByCedula(cedula).orElse(null);
	}
	
	public boolean editarCliente(Client cliente) {
		if (cliente.getEdad() < 18) return false;
		
        Optional<Client> actualOpt = repo.findByCedula(cliente.getCedula());
        if (actualOpt.isEmpty()) return false;
		
        Client actual = actualOpt.get();
        actual.setNombre(cliente.getNombre());
        actual.setApellido(cliente.getApellido());
        actual.setEdad(cliente.getEdad());
        actual.setCiudad(cliente.getCiudad());
        actual.setEstadoMembresia(cliente.isEstadoMembresia());
        actual.setCorreo(cliente.getCorreo());
        actual.setContrasena(cliente.getContrasena());

        repo.save(actual);
        return true;
		
	}
	
	public boolean eliminarCliente(Client cliente) {
        Optional<Client> actual = repo.findByCedula(cliente.getCedula());
        if (actual.isEmpty()) return false;
        repo.delete(actual.get());
        return true;
	}
	
    public Client buscarPorCorreoYContraseña(String correo, String contraseña) {
        return repo.findByCorreoAndContrasena(correo, contraseña).orElse(null);
    }
    
    public ArrayList<Bill> obtenerTodasLasFacturasDelUser(Client cliente) {
        Client c = buscarCliente(cliente.getCedula());
        if (c == null) return new ArrayList<>();
        // `historial` es la relación con Bill (lado cliente) si la dejaste con `mappedBy="cliente"`
        return new ArrayList<Bill>(c.getHistorial()); // sigue devolviendo ArrayList<Bill>
    }
	
    public boolean agregarFacturaClient(Client cliente, Bill factura) {
        Client c = buscarCliente(cliente.getCedula());
        if (c == null) return false;
        // Enlaza ambos lados si tu Bill tiene `cliente`
        factura.setCliente(c);
        c.getHistorial().add(factura);
        repo.save(c);
        return true;
    }
	
}
