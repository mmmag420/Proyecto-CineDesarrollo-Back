package com.example.demo;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import model.Chair;
import model.Hall;
import model.Hall.Dia;

@Repository
public class RepositorySala {
	
	private final ArrayList<Hall> salas = new ArrayList<>();

	public RepositorySala() {
		
	}
	
	public boolean guardarSala(Hall sala) {
		salas.add(sala);
		return true;
	}
	
	public Hall buscarSalaGlobal(int idSala) {
		for(int i = 0; i < salas.size(); i++) {
			if(salas.get(i).getNumSala() == idSala) {
				return salas.get(i);
			}
		}
		return null;
	}
	
	public Hall buscarPorDia(int idSala, Dia diaPelicula) {
		for(int i = 0; i < salas.size(); i++) {
			Hall sala = salas.get(i);
			if(sala.getNumSala() == idSala && sala.getDiaPelicula() == diaPelicula) {
				return sala;
			}	
		}
		return null;
	}
	
	public Hall buscarPorDiaYHora(int idSala, Dia diaPelicula, LocalTime  horaInicio) {
		for(int i = 0; i < salas.size(); i++) {
			Hall sala = salas.get(i);
			if(sala.getNumSala() == idSala && sala.getDiaPelicula() == diaPelicula && sala.getHoraInicio().equals(horaInicio)) {
				return sala;
			}
		}
		return null;
	}
	
	public boolean reservarSilla(Hall sala, int numSilla) {
		Chair[] sillas = sala.getSillas();
		for(int i = 0; i < sillas.length; i++) {
			Chair silla = sillas[i];
			if(silla.getNumSilla() == numSilla && silla.isEstado() == false) {
				silla.setEstado(true);
				return true;
			}
		}
		return false;
	}
	
	public boolean cancelarSilla(Hall sala, int numSilla) {
		Chair[] sillasSala = sala.getSillas();
		for(int i = 0; i < sillasSala.length; i++) {
			if(sillasSala[i].getNumSilla() == numSilla && sillasSala[i].isEstado()) {
			   sillasSala[i].setEstado(false);
			   return true;
			}
		}
		return false;
	}
	
	public boolean[] estadoSillas(Hall sala) {
	   Chair[] sillasSala = sala.getSillas();
	   boolean[] estado = new boolean[sillasSala.length];	   
	   for(int i = 0; i < sillasSala.length; i++) {
		   estado[i] = sillasSala[i].isEstado();		   
	   }
	   return estado;
	}
	
	public List<Hall> listarPorSalaYDia(int idSala, Hall.Dia dia) {
	    List<Hall> out = new ArrayList<>();
	    for (Hall s : salas) if (s.getNumSala()==idSala && s.getDiaPelicula()==dia) out.add(s);
	    return out;
	}
	
	public List<Hall> listarTodas() { return new ArrayList<>(salas); }
	
	
	
	
}
