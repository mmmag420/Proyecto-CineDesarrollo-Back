package com.example.demo;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import model.Chair;
import model.Hall;

@Repository
public class RepositorySala {
	
	private final ArrayList<Hall> funciones = new ArrayList<>();

	public RepositorySala() {
		
	}
	

	private boolean intervalosSeCruzan(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
		return inicio1.isBefore(fin2) && inicio2.isBefore(fin1);
	}	
	

	public boolean crearFuncion(Hall salaNueva) {
		for(int i = 0; i < funciones.size();  i++) {
			Hall salavieja = funciones.get(i);
			if(salavieja.getNumSala() == salaNueva.getNumSala() && salavieja.getDiaPelicula().equals(salaNueva.getDiaPelicula())) {
				
				if(intervalosSeCruzan( LocalTime.parse(salavieja.getHoraInicio()), LocalTime.parse(salavieja.getHoraFin()), LocalTime.parse(salaNueva.getHoraInicio()), LocalTime.parse(salaNueva.getHoraFin()) ) ) {
					return false;
				}

			}
		}
		
		funciones.add(salaNueva);
		return true;
	}
	
	public Hall buscarFuncion(int numSala, String dia, String horaInicio) {
		for(int i = 0; i < funciones.size(); i++) {
			Hall sala = funciones.get(i);
			if(sala.getNumSala() == numSala && sala.getDiaPelicula().equals(dia) && sala.getHoraInicio().equals(horaInicio)) {
				return sala;
			}
		}
		return  null;
	}
	
	public List<Hall> listarPorSalaYDia(int numSala, String dia) {
		List<Hall> lista = new ArrayList<>();
		for(int i = 0; i < funciones.size(); i++) {
			Hall sala = funciones.get(i);
			if(sala.getNumSala() == numSala && sala.getDiaPelicula().equals(dia)) {
				lista.add(sala);
			}
		}
		return lista;
	}
	
	public boolean reservarSilla(int sala, String dia, String horaInicio, int numSilla) {
		Hall salaEx = buscarFuncion(sala, dia, horaInicio);
		if(salaEx == null) {
			return false;
		}
		
		Chair[] sillas = salaEx.getSillas();
		int idx = numSilla - 1;
		if(idx < 0 || idx >= sillas.length) {
			return false;
		}
		
		if(sillas[idx].isEstado()) { 
			return false;
		}
		
		sillas[idx].setEstado(true);
		return true;
	}
	
	public List<Hall> listarPorPeliculaYDia(String peliculaId, String diaISO) {
	    List<Hall> r = new ArrayList<>();
	    for (Hall f : funciones) {
	        if (f.getDiaPelicula().equals(diaISO)
	            && f.getMovie() != null
	            && peliculaId.equals(f.getMovie().getId())) {
	            r.add(f);
	        }
	    }
	    return r;
	}
	
	
}
