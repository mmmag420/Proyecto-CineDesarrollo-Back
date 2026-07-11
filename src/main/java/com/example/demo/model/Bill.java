package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "facturas")
public class Bill {
	
	@Id
	@Column(name = "id_factura")
	private int idFactura;
	
	@Column(name = "cliente_cedula", nullable = false)
	private String cedulaCliente;
	
	@Column(name = "valor_factura", nullable = false)
	private double valorFactura;
	
	@Column(name = "metodo_pago", nullable = false, length = 20)
	private String metodoDePago;
	
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "carrito_id", referencedColumnName = "id_carrito", nullable = false)
	private Car carrito;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    @JsonBackReference
	private Client cliente;
	
	public Bill() {}
	
	public Bill(int idFactura, String cedulaCliente, double valorFactura, String metodoDePago, Car carrito) {
		this.idFactura = idFactura;
		this.cedulaCliente = cedulaCliente;
		this.valorFactura = valorFactura;
		this.metodoDePago = metodoDePago;
		this.carrito = carrito;
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

	public Client getCliente() {
		return cliente;
	}

	public void setCliente(Client cliente) {
		this.cliente = cliente;
	}
	

}
