package com.example.demo.servicios;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.example.demo.repositorios.RepositoryMovie;
import com.example.demo.repositorios.RepositorySala;
import com.example.demo.servicios.ServiceMovie;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;


import com.example.demo.model.Chair;
import com.example.demo.model.Hall;
import com.example.demo.model.Movie;
import com.example.demo.model.Hall.Dia;



@Service
@Transactional
@DependsOn("serviceMovie")
public class ServiceSala {

	
	private final RepositorySala repoSala;
	private final ServiceMovie serviceMovie;
	private final RepositoryMovie repoMovie;
	
	public ServiceSala(RepositorySala repoSala, ServiceMovie serviceMovie, RepositoryMovie repoMovie) {
		this.repoSala = repoSala;
		this.serviceMovie = serviceMovie;	
		this.repoMovie = repoMovie;
	}
	
	public void iniciarBaseQuemada() {
		
        if (!repoSala.findAll().isEmpty()) {
            return;
        }
		
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
	
    public List<Hall> listarTodas() {
        return repoSala.findAll();
    }
	
    public boolean guardarSala(Hall sala) {
        Optional<Hall> encontrado = repoSala.findByNumSalaAndDiaPeliculaAndHoraInicio(sala.getNumSala(), sala.getDiaPelicula(), sala.getHoraInicio());
        if(encontrado.isPresent()) {
        	return false;
        }        
        repoSala.save(sala);
        return true;
    }
	
	//BUSCA SALA EN TODAS
	public Hall buscarSalaGlobal(int idSala) {
	   return repoSala.findById(idSala).orElse(null);
	    
	}

	//BUSCA LA SALA POR DIA
	public Hall buscarPorDia(int idSala, Dia diaPelicula) {
		return repoSala.findByNumSalaAndDiaPelicula(idSala, diaPelicula).orElse(null);

	}
	
	//BUSCA LA SALA POR DIA Y HORA	
    public Hall buscarPorDiaYHora(int numSala, Dia dia, LocalTime horaInicio) {
        return repoSala.findByNumSalaAndDiaPeliculaAndHoraInicio(numSala, dia, horaInicio).orElse(null);
    }
	
	//RESERVA SILLA EN LA SALA
    @Transactional
    public boolean reservarSilla(Hall sala, int numSilla) {
        // Garantiza array de 39 y sillas no nulas
        Chair[] sillas = sala.getSillas();
        if (sillas == null || sillas.length != 39) {
            sillas = new Chair[39];
            sala.setSillas(sillas);
        }
        for (int i = 0; i < sillas.length; i++) {
            if (sillas[i] == null) {
                sillas[i] = new Chair(i + 1, false);
            }
        }

        // Rango válido 1..39
        if (numSilla < 1 || numSilla > 39) return false;

        // Pequeño cerrojo por instancia (sin crear campos)
        synchronized (sala) {
            Chair silla = sillas[numSilla - 1];
            if (silla.isEstado()) return false; // ya ocupada
            silla.setEstado(true);
            return true;
        }
    }
	
	//CANCELAR SILLA POR SI LA QUITA DEL CARRITO
	   public boolean cancelarSilla(Hall sala, int numSilla) {
	        Chair[] sillas = sala.getSillas();
	        if (sillas == null) return false;
	        for (Chair s : sillas) {
	            if (s.getNumSilla() == numSilla && s.isEstado()) {
	                s.setEstado(false);
	                return true;
	            }
	        }
	        return false;
	    }
	
	//DEVUELVE EL ESTADO DE LAS SILLAS DE LA SALA PARA MOSTRARLAS EN LA VENTANA
	public boolean[] estadoSillas(Hall sala) {
	    Chair[] sillasSala = sala.getSillas();
	    if (sillasSala == null || sillasSala.length != 39) {
	        sillasSala = new Chair[39];
	        sala.setSillas(sillasSala);
	    }
	    for (int i = 0; i < sillasSala.length; i++) {
	        if (sillasSala[i] == null) {
	            sillasSala[i] = new Chair(i + 1, false);
	        }
	    }
	    boolean[] estado = new boolean[sillasSala.length];
	    for (int i = 0; i < sillasSala.length; i++) {
	        estado[i] = sillasSala[i].isEstado();
	    }
	    return estado;
	}
	
	public List<Hall> listarPorSalaYDia(int idSala, Hall.Dia dia) {
	    List<Hall> out = new ArrayList<>();
	    List<Hall> salas = repoSala.findAll();
	    for (Hall s : salas) if (s.getNumSala()==idSala && s.getDiaPelicula()==dia) out.add(s);
	    return out;
	}
		

	// GENERA SILLAS EN FALSE PARA CADA SALA
    private Chair[] generarSillas(int cantidad) {
        Chair[] sillas = new Chair[cantidad];
        for (int i = 0; i < sillas.length; i++) {
        
            sillas[i] = new Chair(i + 1, false); 
        }
        return sillas;
    }
    
    public int limpiarSalaPorId(int idObjetivo) {
        int count = 0;

        for (Hall h : repoSala.findAll()) {
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
    
    @PostConstruct
    public void initBase() {
        iniciarBaseQuemada();
    }
    
    @Transactional
    public int pasarSalasDePelicula(String movieIdVieja, String movieIdNueva) {
        Movie vieja = repoMovie.findById(movieIdVieja).orElse(null);
        Movie nueva = repoMovie.findById(movieIdNueva).orElse(null);
        if (vieja == null || nueva == null) return 0;

        List<Hall> salas = repoSala.findByMovie_Id(movieIdVieja);
        for (Hall h : salas) {
            h.setMovie(nueva);         
        }
        repoSala.saveAll(salas);
        return salas.size();
    }
    


}
