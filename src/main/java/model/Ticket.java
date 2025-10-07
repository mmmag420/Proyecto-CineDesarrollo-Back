package model;

public class Ticket {
	
	private int numEntrada;
	private int precioEntrada;
	private Hall sala;
	
	public Ticket(int numEntrada, int precioEntrada, Hall sala) {
		this.numEntrada = numEntrada;
		this.precioEntrada = precioEntrada;
		this.sala = sala;
	}

	public int getNumEntrada() {
		return numEntrada;
	}

	public void setNumEntrada(int numEntrada) {
		this.numEntrada = numEntrada;
	}

	public int getPrecioEntrada() {
		return precioEntrada;
	}

	public void setPrecioEntrada(int precioEntrada) {
		this.precioEntrada = precioEntrada;
	}

	public Hall getSala() {
		return sala;
	}

	public void setSala(Hall sala) {
		this.sala = sala;
	}

}
