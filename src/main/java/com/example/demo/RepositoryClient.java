package com.example.demo;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import model.Client;

@Repository
public class RepositoryClient {

	private final ArrayList<Client> listaClientes;

	public RepositoryClient() {
		this.listaClientes = new ArrayList<>();
	}
	
	public List<Client> listar() {
		return new ArrayList<>(listaClientes);
	}
	
	
	
	
	public boolean guardar(Client cliente) {
		listaClientes.add(cliente);
		return true;
	}
	
	public Client buscar(String cedula) {
		for(int i = 0; i < listaClientes.size(); i++) {
			if(listaClientes.get(i).getCedula().equals(cedula)) {
				return listaClientes.get(i);
			}
		}
		return null;
	}
	
	public boolean editar(Client cliente) {
		Client aux = buscar(cliente.getCedula());
		
		if(aux != null) {
            aux.setNombre(cliente.getNombre());
            aux.setApellido(cliente.getApellido());
            aux.setEdad(cliente.getEdad());
            aux.setCiudad(cliente.getCiudad());
            aux.setEstadoMembresia(cliente.isEstadoMembresia());
            aux.setCorreo(cliente.getCorreo());
            aux.setContraseña(cliente.getContraseña());
            return true;
		}
		return false;
	}
	
	public boolean eliminar(Client cliente) {
		for(int i = 0; i < listaClientes.size(); i++) {
			if(listaClientes.get(i).getCedula().equals(cliente.getCedula())) {
				listaClientes.remove(i);
				return true;
			}
		}
		return false;
	}

	public ArrayList<Client> getListaClientes() {
		return listaClientes;
	}
	
	public Client buscarPorCorreoYContraseña(String correo, String contraseña) {		
		for(int i = 0; i < listaClientes.size(); i++) {
			if(listaClientes.get(i).getCorreo().equals(correo) && listaClientes.get(i).getContraseña().equals(contraseña)) {
				return listaClientes.get(i);
			}
		}
		return null;
	}



	

}
