package com.example.demo.servicios;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import com.example.demo.repositorios.RepositoryMovie;
import com.example.demo.repositorios.RepositorySala;
import com.example.demo.repositorios.RepositorySilla;
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
	private final RepositorySilla repoSilla;
	
	public ServiceSala(RepositorySala repoSala, ServiceMovie serviceMovie, RepositoryMovie repoMovie, RepositorySilla repoSilla) {
		this.repoSala = repoSala;
		this.serviceMovie = serviceMovie;	
		this.repoMovie = repoMovie;
		this.repoSilla = repoSilla;
	}
	
	
	public void iniciarBaseQuemada() {

        if (!repoSala.findAll().isEmpty()) {
            return;
        }
		
		Movie nobody2 = serviceMovie.findById(1);
		Movie conjuro2 = serviceMovie.findById(2);
		Movie cuatrofantasticos = serviceMovie.findById(3);
		
        var salasConfig = Map.of(
                1, new SalaConfig(nobody2, 39),
                2, new SalaConfig(conjuro2,   39),
                3, new SalaConfig(cuatrofantasticos,  39)
            );
        
        
        LocalTime[] horarios = {
                LocalTime.of(16, 50),
                LocalTime.of(21, 30)                
            };
        
        for (Map.Entry<Integer, SalaConfig> entry : salasConfig.entrySet()) {
        	
            int numSala = entry.getKey();
            Movie movie = entry.getValue().movie();
            int capacidad = entry.getValue().capacidad();
            
            for (Hall.Dia dia : Hall.Dia.values()) {
                for (LocalTime hi : horarios) {
                    LocalTime hf = calcularHoraFin(hi, movie, 15); 
                    Hall hall = new Hall(numSala, movie, dia, hi, hf, null);
                    List<Chair> sillas = generarSillas(capacidad, hall);
                    hall.setSillas(sillas);
                   
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
	
    
	public Hall buscarSalaGlobal(int idSala) {
	   return repoSala.findById(idSala).orElse(null);
	    
	}

	
	public Hall buscarPorDia(int idSala, Dia diaPelicula) {
		return repoSala.findByNumSalaAndDiaPelicula(idSala, diaPelicula).orElse(null);

	}
	
	
    public Hall buscarPorDiaYHora(int numSala, Dia dia, LocalTime horaInicio) {
        return repoSala.findByNumSalaAndDiaPeliculaAndHoraInicio(numSala, dia, horaInicio).orElse(null);
    }
	
	
    @Transactional
    public boolean reservarSilla(Hall sala, int numSilla) {
        if (numSilla < 1 || numSilla > 39) {
            return false;
        }
        
        asegurarSillas(sala);
        
        Chair silla = sala.getSillas().stream()
                .filter(c -> c.getNumSilla() == numSilla)
                .findFirst()
                .orElse(null);
        
        if (silla == null) return false;
        if (silla.isEstado()) return false;
        
        silla.setEstado(true);
        
        repoSala.save(sala);
        
        return true;

    }
	
	
	   public boolean cancelarSilla(Hall sala, int numSilla) {
		   if (sala.getSillas() == null || sala.getSillas().isEmpty()) {
		        return false;
		    }

		    Chair silla = sala.getSillas().stream()
		            .filter(c -> c.getNumSilla() == numSilla)
		            .findFirst()
		            .orElse(null);

		    if (silla == null) return false;
		    if (!silla.isEstado()) return false; 

		    silla.setEstado(false);
		    repoSala.save(sala);

		    return true;
    
	    }
	

	public boolean[] estadoSillas(Hall sala) {
		asegurarSillas(sala);
		
		boolean[] estado = new boolean[39];
		
	    for (Chair c : sala.getSillas()) {
	        int n = c.getNumSilla(); // 
	        estado[n - 1] = c.isEstado();
	    }
	    
	    return estado;
	}
	
	
	public List<Hall> listarPorSalaYDia(int idSala, Hall.Dia dia) {
	    List<Hall> out = new ArrayList<>();
	    List<Hall> salas = repoSala.findAll();
	    for (Hall s : salas) if (s.getNumSala()==idSala && s.getDiaPelicula()==dia) out.add(s);
	    return out;
	}
		

    private List<Chair> generarSillas(int cantidad, Hall hall) {
    	List<Chair> sillas = new ArrayList<Chair>();
        for (int i = 0; i < cantidad; i++) {        
        	sillas.add(new Chair(i + 1, false, hall));
        }
        return sillas;
    }
    
    public int limpiarSalaPorId(int idObjetivo) {
    	  int count = 0;

    	    for (Hall h : repoSala.findAll()) {
    	        if (h.getNumSala() == idObjetivo) {
    	            if (h.getSillas() != null) {
    	                for (Chair c : h.getSillas()) {
    	                    c.setEstado(false);
    	                }
    	                repoSala.save(h); 
    	            }
    	            count++;
    	        }
    	    }

    	    return count;
    }
           
    private record SalaConfig(Movie movie, int capacidad) {}

    private LocalTime calcularHoraFin(LocalTime horaInicio, Movie movie, int limpiezaMin) {
        int duracionMin = convertirDuracionAMinutos(movie.getDuracion());
        return horaInicio.plusMinutes(duracionMin + limpiezaMin);
    }


    private int convertirDuracionAMinutos(String duracionStr) {
        duracionStr = duracionStr.trim().toLowerCase();

        int horas = 0;
        int minutos = 0;

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
            String minStr = duracionStr.replace("m", "").trim();
            minutos = Integer.parseInt(minStr);
        }

        return horas * 60 + minutos;
    }
    
    
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
    public int crearFuncionesPorDefectoParaMovie(Integer movieId, int numSala, int capacidad) {
        Movie movie = repoMovie.findById(movieId).orElse(null);
        if (movie == null) return 0;

        LocalTime[] horarios = { LocalTime.of(16,50), LocalTime.of(21,30) };
        int creadas = 0;
        for (Hall.Dia dia : Hall.Dia.values()) {
            for (LocalTime hi : horarios) {
                LocalTime hf = calcularHoraFin(hi, movie, 15);
                Hall hall = new Hall(numSala, movie, dia, hi, hf, null);
                hall.setSillas(generarSillas(capacidad, hall));
                repoSala.save(hall);
                creadas++;
            }
        }
        return creadas;
    }
    
    
    @Transactional
    public int crearFuncionesPorDefectoParaMovieAutoSala(Integer movieId, Integer capacidad) {
        Movie movie = repoMovie.findById(movieId).orElse(null);
        if (movie == null) {
        	return 0;
        }

        int max = Optional.ofNullable(repoSala.findMaxNumSala()).orElse(0);
        int nextNumSala = max + 1;
        int cap = (capacidad != null && capacidad > 0) ? capacidad : 39;

        return crearFuncionesPorDefectoParaMovie(movie.getId(), nextNumSala, cap);
    }
    
    
    private void asegurarSillas(Hall sala) {
        if (sala.getSillas() == null) {
            sala.setSillas(new ArrayList<>());
        }

        if (sala.getSillas().size() == 39) {
            return;
        }

        Set<Integer> existentes = sala.getSillas()
                .stream()
                .map(Chair::getNumSilla)
                .collect(Collectors.toSet());

        for (int i = 1; i <= 39; i++) {
            if (!existentes.contains(i)) {
                sala.getSillas().add(new Chair(i, false, sala));
            }
        }
    }

}
