package model;

public class Chair {
	
	private int numSilla;
	private boolean estado;
	
	public Chair(int numSilla, boolean estado) {
		this.numSilla = numSilla;
		this.estado = estado;
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
