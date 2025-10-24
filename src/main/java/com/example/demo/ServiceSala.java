package com.example.demo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import model.Chair;
import model.Hall;
import model.Movie;
import model.Hall.Dia;

@Service
public class ServiceSala {

	
	private final RepositorySala repoSala;
	private final ServiceMovie serviceMovie;
	
	public ServiceSala(RepositorySala repoSala, ServiceMovie serviceMovie) {
		this.repoSala = repoSala;
		this.serviceMovie = serviceMovie;	
		iniciarBaseQuemada();
	}
	
	public void iniciarBaseQuemada() {
		
		Movie nobody2 = serviceMovie.findById("1");
		Movie conjuro2 = serviceMovie.findById("2");
		Movie cuatrofantasticos = serviceMovie.findById("3");
		
        var salasConfig = Map.of(
                1, new SalaConfig(nobody2, 39),
                2, new SalaConfig(conjuro2,   39),
                3, new SalaConfig(cuatrofantasticos,  39)
            );
        
        
        LocalTime[] horarios = {
                LocalTime.of(16, 50),
                LocalTime.of(21, 30)                
            };
        
        //definimos los dos horarios diaros de funciones 4:50 y 9:30 con localtime
        for (Map.Entry<Integer, SalaConfig> entry : salasConfig.entrySet()) {
        	
            int numSala = entry.getKey();
            Movie movie = entry.getValue().movie();
            int capacidad = entry.getValue().capacidad();
            
            for (Hall.Dia dia : Hall.Dia.values()) {
                for (LocalTime hi : horarios) {
                    LocalTime hf = calcularHoraFin(hi, movie, 15); 
                    Hall hall = new Hall(numSala, movie, dia, hi, hf, generarSillas(capacidad));

                   
                    boolean ok = guardarSala(hall);
                    
                }
            }
		
        }

	}

	//GUARDA LA SALA
	public boolean guardarSala(Hall sala) {
		Hall encontrado = repoSala.buscarPorDiaYHora(sala.getNumSala(), sala.getDiaPelicula(), sala.getHoraInicio());
		if(encontrado != null) {
			return false;
		}
		return repoSala.guardarSala(sala);
	}
	
	//BUSCA SALA EN TODAS
	public Hall buscarSalaGlobal(int idSala) {
		Hall encontrado = repoSala.buscarSalaGlobal(idSala);
		if(encontrado == null) {
			return null;		
		}
		return encontrado;
	}

	//BUSCA LA SALA POR DIA
	public Hall buscarPorDia(int idSala, Dia diaPelicula) {
		Hall encontrado = repoSala.buscarPorDia(idSala, diaPelicula);
		if(encontrado == null) {
			return null;
		}
		return encontrado;
	}
	
	//BUSCA LA SALA POR DIA Y HORA
	public Hall buscarPorDiaYHora(int idSala, Dia diaPelicula, LocalTime  horaInicio) {
		Hall encontrado = repoSala.buscarPorDiaYHora(idSala, diaPelicula, horaInicio);
		if(encontrado == null) {
			return null;
		}
		return encontrado;
	}
	
	//RESERVA SILLA EN LA SALA
	public boolean reservarSilla(Hall sala, int numSilla) {
		return repoSala.reservarSilla(sala, numSilla);
	}
	
	//CANCELAR SILLA POR SI LA QUITA DEL CARRITO
	public boolean cancelarSilla(Hall sala, int numSilla) {
		return repoSala.cancelarSilla(sala, numSilla);
	}
	
	//DEVUELVE EL ESTADO DE LAS SILLAS DE LA SALA PARA MOSTRARLAS EN LA VENTANA
	public boolean[] estadoSillas(Hall sala) {
		  return repoSala.estadoSillas(sala);
	}
	
	//LISTA LAS SALAS POR DIA 
	public List<Hall> listarPorSalaYDia(int idSala, Hall.Dia dia) {
	    return repoSala.listarPorSalaYDia(idSala, dia);
	}
	
	//LISTA TODAS LAS SALAS
	public List<Hall> listarTodas() {
		return repoSala.listarTodas();
	}
	

	// GENERA SILLAS EN FALSE PARA CADA SALA
    private Chair[] generarSillas(int cantidad) {
        Chair[] sillas = new Chair[cantidad];
        for (int i = 0; i < sillas.length; i++) {
        
            sillas[i] = new Chair(i + 1, false); 
        }
        return sillas;
    }
    
    //LIMPIA LA SALA POR ID
    public int limpiarSalaPorId(int idObjetivo) {
        int count = 0;

        for (Hall h : repoSala.listarTodas()) {
            if (h.getNumSala() == idObjetivo) {
                for (Chair c : h.getSillas()) {
                    c.setEstado(false); // Limpia la silla (la marca como vacía)
                }
                count++;
            }
        }

        return count;
    }
    
    
    private record SalaConfig(Movie movie, int capacidad) {}

    // CALCULAMOS A QUE HORA SE ESTA ACABANDO LA PELICULA
    private LocalTime calcularHoraFin(LocalTime horaInicio, Movie movie, int limpiezaMin) {
        int duracionMin = convertirDuracionAMinutos(movie.getDuracion());
        return horaInicio.plusMinutes(duracionMin + limpiezaMin);
    }

    //CONVERTIMOS LA DURACION DE LAS PELCICULAS QUE ESTAN EN STRING A MINUTOS PARA PODER COMPARAR EN OTROS METODOS
    private int convertirDuracionAMinutos(String duracionStr) {
        // Elimina espacios extra y convierte todo a minúsculas
        duracionStr = duracionStr.trim().toLowerCase();

        int horas = 0;
        int minutos = 0;

        // Ejemplo de formato: "1h 55m" o "2h10m"
        if (duracionStr.contains("h")) {
            String[] partes = duracionStr.split("h");
            try {
                horas = Integer.parseInt(partes[0].trim());
            } catch (NumberFormatException e) {
                horas = 0;
            }

            if (partes.length > 1 && partes[1].contains("m")) {
                String minStr = partes[1].replace("m", "").trim();
                if (!minStr.isEmpty()) {
                    try {
                        minutos = Integer.parseInt(minStr);
                    } catch (NumberFormatException e) {
                        minutos = 0;
                    }
                }
            }
        } else if (duracionStr.contains("m")) {
            // Solo minutos, ejemplo: "95m"
            String minStr = duracionStr.replace("m", "").trim();
            minutos = Integer.parseInt(minStr);
        }

        return horas * 60 + minutos;
    }
    
    // CASTEAR EL ENUM DIAS EN DAYOFWEEK
    
    private java.time.DayOfWeek toDow(Hall.Dia d) {
        switch (d) {
            case LUNES: return java.time.DayOfWeek.MONDAY;
            case MARTES: return java.time.DayOfWeek.TUESDAY;
            case MIERCOLES: return java.time.DayOfWeek.WEDNESDAY;
            case JUEVES: return java.time.DayOfWeek.THURSDAY;
            case VIERNES: return java.time.DayOfWeek.FRIDAY;
            case SABADO: return java.time.DayOfWeek.SATURDAY;
            case DOMINGO: return java.time.DayOfWeek.SUNDAY;
            default: throw new IllegalStateException();
        }
    }

}
