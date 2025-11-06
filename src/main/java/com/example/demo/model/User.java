package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User {
	
	@Column(nullable = false, length = 20)
	private String rol;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
	private String correo;
    
    @NotBlank
    @Size(min = 8)
    @Column(name = "contrasena", nullable = false)                // <-- columna en DB (ASCII)
    @JsonProperty(value = "contraseña", access = JsonProperty.Access.WRITE_ONLY)
	private String contrasena;
	
	public User() {}

	public User(String rol, int id, String correo, String contrasena) {
		this.rol = rol;
		this.id = id;
		this.correo = correo;
		this.contrasena = contrasena;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contraseña) {
		this.contrasena = contraseña;
	}
	
 
}
