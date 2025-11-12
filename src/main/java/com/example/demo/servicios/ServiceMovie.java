package com.example.demo.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repositorios.RepositoryMovie;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import com.example.demo.model.Movie;

@Service
@Transactional
public class ServiceMovie {

	private final RepositoryMovie repositorymovie;
	
	@Autowired
	public ServiceMovie (RepositoryMovie repositorymovie) {
		this.repositorymovie = repositorymovie;
	}
	
	@PostConstruct
	private void initSampleData() {
		upsertIfAbsent(new Movie(
	        "Nadie 2",
	        "Hutch regresa enfrentándose a nuevos enemigos tras sus vacaciones familiares.",
	        "Acción, Comedia, Thriller, Crimen",
	        "Bob Odenkirk",
	        "Timo Tjahjanto",
	        "/imagenes/posterNadie2.jpg",
	        "/imagenes/btnNadie2.png",
	        "https://www.youtube.com/watch?v=latAzkdZJO4&ab_channel=RoyalFilms",
	        "1h 55m",
	        true
	    ));
	    
		upsertIfAbsent(new Movie(
	            "El Conjuro",
	            "Los Warren investigan un nuevo caso sobrenatural lleno de terror y misterio.",
	            "Terror",
	            "Vera Farmiga, Patrick Wilson",
	            "James Wan",
	            "/imagenes/posterConjuro.jpg",
	            "/imagenes/btnElConjuro.png",
	            "https://www.youtube.com/watch?v=pZGe0V7_L-Q&ab_channel=RoyalFilms",
	            "2h 05m",
	            true
	    ));
	    
		upsertIfAbsent(new Movie(				
	            "Los Cuatro Fantásticos",
	            "Un grupo de superhéroes con poderes extraordinarios lucha contra nuevas amenazas.",
	            "Acción, Aventura, Ciencia Ficción",
	            "Ioan Gruffudd, Jessica Alba, Chris Evans, Michael Chiklis",
	            "Matt Shakman",
	            "/imagenes/poster4Fantasticos.jpg",
	            "/imagenes/btnCuatroFantasticos.png",
	            "https://www.youtube.com/watch?v=g-a8Db2xea0&ab_channel=RoyalFilms",
	            "2h 10m",
	            true
	            
	    ));

	}
	
    public List<Movie> findAll() {
        return repositorymovie.findAll();
    }
	
	
	public Movie save(Movie movie) {
		return repositorymovie.save(movie);
	}
	
	public Movie findById (int id) {
		return repositorymovie.findById(id).orElse(null);
	}
	
    public Movie update(Movie movie) {
        Optional<Movie> actualOpt = repositorymovie.findById(movie.getId());
        if (actualOpt.isEmpty()) return null;

        Movie actual = actualOpt.get();
        actual.setNombre(movie.getNombre());
        actual.setDescripcion(movie.getDescripcion());
        actual.setDuracion(movie.getDuracion());
        actual.setClasificacion(movie.getClasificacion());
        actual.setDirector(movie.getDirector());
        actual.setReparto(movie.getReparto());
        actual.setRutaImagen(movie.getRutaImagen());
        actual.setRutaImagenBoton(movie.getRutaImagenBoton());
        actual.setTrailer(movie.getTrailer());
        actual.setEstado(movie.isEstado());

        repositorymovie.save(actual);
        return actual;
    }
	
    public boolean deleteById(int id) {
        Optional<Movie> actual = repositorymovie.findById(id);
        if (actual.isEmpty()) return false;
        repositorymovie.delete(actual.get());
        return true;
    }
    
    private void upsertIfAbsent(Movie m) {
        if (!repositorymovie.existsById(m.getId())) {
            repositorymovie.save(m);
        }
    }
	
	/*public List<Movie> buscarPorFiltros(String descripcion, String clasificacion) {
        return repositorymovie.buscarPorFiltros(descripcion, clasificacion);
    }
	
	public Movie findByAuthToken (String authToken) {
		return repositorymovie.findByAuthToken(authToken);
	}*/
    
  
    	

}
