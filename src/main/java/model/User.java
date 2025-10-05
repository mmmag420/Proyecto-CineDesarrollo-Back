package model;

public class User {

	private String rol;
	private int id;
	private String correo;
	private String contraseña;

	public User(String rol, int id, String correo, String contraseña) {
		this.rol = rol;
		this.id = id;
		this.correo = correo;
		this.contraseña = contraseña;
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

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}
	
    


	



    
	
	
	
    
	
	
	
	
	
}
