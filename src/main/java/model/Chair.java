package model;

public class Chair {
	
	private int numSilla;
	private boolean estado;
	
	//private Hall sala; quitarla si estoy seguro de que esa es la relacion de chair sala y sala chair, aunque no cambie de vector a list en la clase sala
	
	public Chair() {}
	
	public Chair(int numSilla, boolean estado) {
		this.numSilla = numSilla;
		this.estado = estado;
		//this.sala = sala;
		
	}

	public int getNumSilla() {
		return numSilla;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	
	
}
