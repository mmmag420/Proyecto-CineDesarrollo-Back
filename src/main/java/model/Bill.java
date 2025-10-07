package model;

public class Bill {
	
	private int idFactura;
	private int usuarioid;
	private double valorFactura;
	private String metodoDePago;
	
	public Bill(int idFactura, int usuarioid, double valorFactura, String metodoDePago) {
		this.idFactura = idFactura;
		this.usuarioid = usuarioid;
		this.valorFactura = valorFactura;
		this.metodoDePago = metodoDePago;
	}

	public int getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(int idFactura) {
		this.idFactura = idFactura;
	}

	public int getUsuarioid() {
		return usuarioid;
	}

	public void setUsuarioid(int usuarioid) {
		this.usuarioid = usuarioid;
	}

	public double getValorFactura() {
		return valorFactura;
	}

	public void setValorFactura(double valorFactura) {
		this.valorFactura = valorFactura;
	}

	public String getMetodoDePago() {
		return metodoDePago;
	}

	public void setMetodoDePago(String metodoDePago) {
		this.metodoDePago = metodoDePago;
	}

}
