package model;

public class Food {

	private int idCombo;
	private double precio;
	private String descripcion;
	
	public Food(int idCombo, double precio, String descripcion) {
		this.idCombo = idCombo;
		this.precio = precio;
		this.descripcion = descripcion;
	}

	public int getIdCombo() {
		return idCombo;
	}


	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getDescripcion() {
		return descripcion;
	}

	
	
	
	
}
