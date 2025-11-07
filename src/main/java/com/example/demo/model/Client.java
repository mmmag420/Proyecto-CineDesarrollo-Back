package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Client extends User {

	@Column(nullable = false, unique = true, length = 20)
	private String cedula;
	
	@Column(nullable = false, length = 60)
	private String nombre;
	
	@Column(nullable = false, length = 60)
	private String apellido;
	
	@Column(nullable = false)
	private int edad;
	
	@Column(nullable = false, length = 80)
	private String ciudad;
	
	@Column(nullable = false)
	private boolean estadoMembresia;
	
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Bill> historial = new ArrayList<Bill>();
	

	
	public Client() {}
	
	public Client(String rol, int id, String correo, String contraseña, String cedula, String nombre, String apellido, int edad, String ciudad, boolean estadoMembresia, List<Bill> historial) {
		super(rol, id, correo, contraseña);
		this.cedula = cedula;
		this.nombre = nombre;
		this.apellido = apellido;
		this.edad = edad;
		this.ciudad = ciudad;
		this.estadoMembresia = estadoMembresia;
		this.historial = (historial == null) ? new ArrayList<Bill>() : historial;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public boolean isEstadoMembresia() {
		return estadoMembresia;
	}

	public void setEstadoMembresia(boolean estadoMembresia) {
		this.estadoMembresia = estadoMembresia;
	}

	public List<Bill> getHistorial() {
		return historial;
	}

	public void setHistorial(List<Bill> historial) {
		this.historial = historial;
	}

	
}
