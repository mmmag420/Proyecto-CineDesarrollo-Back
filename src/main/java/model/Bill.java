package model;

import java.util.ArrayList;

public class Bill {
	
	private int idFactura;
	private String cedulaCliente;
	private double valorFactura;
	private String metodoDePago;
	private Car carrito;
	
	//private Client cliente; relacion de bill a cliente y cliente a bill quitar cuando este seguro de como se maneja esta relacion
	
	public Bill(int idFactura, String cedulaCliente, double valorFactura, String metodoDePago, Car carrito) {
		this.idFactura = idFactura;
		this.cedulaCliente = cedulaCliente;
		this.valorFactura = valorFactura;
		this.metodoDePago = metodoDePago;
		this.carrito = carrito;
		//this.cliente = cliente;
	}

	public int getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(int idFactura) {
		this.idFactura = idFactura;
	}

	public String getCedulaCliente() {
		return cedulaCliente;
	}

	public void setCedulaCliente(String cedulaCliente) {
		this.cedulaCliente = cedulaCliente;
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

	public Car getCarrito() {
		return carrito;
	}

	public void setCarrito(Car carrito) {
		this.carrito = carrito;
	}

	
	

}
