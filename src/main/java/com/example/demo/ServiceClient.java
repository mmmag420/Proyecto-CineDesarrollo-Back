package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import model.Bill;
import model.Client;

@Service
public class ServiceClient {

	private final RepositoryClient repo;

	public ServiceClient(RepositoryClient repo) {
		this.repo = repo;
		iniciarBaseQuemada();
	}
	
	private void iniciarBaseQuemada() {
		Client mateo = new Client("Cliente", 1, "albornoz.mateo.3059@eam.edu.co", "mateo123", "1034290939", "Mateo", "Albornoz", 20, "Armenia", false, null);
		Client sara = new Client("Cliente", 2, "reyes.sara.3050@eam.edu.co", "sara123", "1092290939", "Sara", "Reyes", 19, "Armenia", false, null);
		Client parra = new Client("Cliente", 3, "parra.david.3052@eam.edu.co", "parra123", "1033290939", "Juan", "Parra", 18, "Armenia", false, null);
		Client salome = new Client("Cliente", 4, "trujillo.salome.3051@eam.edu.co", "salome123", "1045290939", "Salome", "Trujillo", 20, "Armenia", false, null);
		
		guardarCliente(mateo);
		guardarCliente(sara);
		guardarCliente(parra);
		guardarCliente(salome);
	}
	
	public List<Client> listarClientes() {
		return repo.listar();
	}

	
	public boolean guardarCliente(Client cliente) {
		
		Client client = repo.buscar(cliente.getCedula());	
		
		if(client != null) {
			return false;
		}		
		
		if(cliente.getEdad() < 18) {
			return false;
		}	
		
		return repo.guardar(cliente);
	}
	
	public Client buscarCliente(String cedula) {
		Client cliente = repo.buscar(cedula);
		if(cliente == null) {
			return null;
		}
		return cliente;
	}
	
	public boolean editarCliente(Client cliente) {
		boolean editado = repo.editar(cliente);
		if(!editado) {
		   return false;
		}
		
		if(cliente.getEdad() <= 0 || cliente.getEdad() < 18) {
			return false;
		}
		
		return repo.guardar(cliente);
		
	}
	
	public boolean eliminarCliente(Client cliente) {
		Client encontrado = repo.buscar(cliente.getCedula());
		if(encontrado == null) {
			return false;
		}
		return repo.eliminar(cliente);
	}
	
	public Client buscarPorCorreoYContraseña(String correo, String contraseña) {
		Client cliente = repo.buscarPorCorreoYContraseña(correo, contraseña);
		if(cliente == null) {
			return null;
		}
		return cliente;
	}
	
	public ArrayList<Bill> obtenerTodasLasFacturasDelUser(Client cliente) {
		return repo.listarFacturasDelUser(cliente);
	}
	
	public boolean agregarFacturaClient(Client cliente, Bill factura) {
		Client encontrado = repo.buscar(cliente.getCedula());
		if(encontrado == null) {
			return false;
		}
		return repo.agregarFacturaClient(encontrado, factura);
	}
	
}
