package model;

import java.util.ArrayList;

public class Client extends User {

	private String cedula;
	private String nombre;
	private String apellido;
	private int edad;
	private String ciudad;
	private boolean estadoMembresia;
	private ArrayList<Bill> historial;
	private Car carrito;
	
	public Client(String rol, int id, String correo, String contraseña, String cedula, String nombre, String apellido, int edad, String ciudad, boolean estadoMembresia, ArrayList<Bill> historial, Car carrito) {
		super(rol, id, correo, contraseña);
		this.cedula = cedula;
		this.nombre = nombre;
		this.apellido = apellido;
		this.edad = edad;
		this.ciudad = ciudad;
		this.estadoMembresia = estadoMembresia;
		this.historial = historial;
		this.carrito = carrito;
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

	public ArrayList<Bill> getHistorial() {
		return historial;
	}

	public void setHistorial(ArrayList<Bill> historial) {
		this.historial = historial;
	}

	public Car getCarrito() {
		return carrito;
	}

	public void setCarrito(Car carrito) {
		this.carrito = carrito;
	}
	
	
	
    
    
	
    
	
	
    
 
    

	
	
}
