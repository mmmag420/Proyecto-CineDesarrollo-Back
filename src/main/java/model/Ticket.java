package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "entradas")
public class Ticket {
	
	@Id
    @Column(name = "num_entrada", nullable = false)
	private int numEntrada;
	
    @NotNull
    @Column(name = "precio_entrada", nullable = false, precision = 10, scale = 2)
	private double precioEntrada;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sala_id", referencedColumnName = "num_sala", nullable = false)
	private Hall sala;
	
	public Ticket() {}
	
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

	public double getPrecioEntrada() {
		return precioEntrada;
	}

	public void setPrecioEntrada(double precioEntrada) {
		this.precioEntrada = precioEntrada;
	}

	public Hall getSala() {
		return sala;
	}

	public void setSala(Hall sala) {
		this.sala = sala;
	}

}
