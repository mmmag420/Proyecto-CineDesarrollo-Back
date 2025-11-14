package com.example.demo.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Client;

public interface RepositoryClient extends JpaRepository<Client, Integer> {
	
	Optional<Client> findByCedula(String cedula);
	boolean existsByCedula(String cedula);
	
	Optional<Client> findByCorreoAndContrasena(String correo, String contrasena);
	boolean existsByCorreoAndContrasena(String correo, String contrasena);
	
	Optional<Client> findByCorreo(String correo);
		
}
