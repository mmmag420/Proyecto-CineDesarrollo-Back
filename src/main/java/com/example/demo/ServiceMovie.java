package com.example.demo;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import model.Movie;

@Service
public class ServiceMovie {

	private final RepositoryMovie repositorymovie;
	
	@Autowired
	public ServiceMovie (RepositoryMovie repositorymovie) {
		this.repositorymovie = repositorymovie;
		initSampleData();
	}
	
	private void initSampleData() {
	    Movie Nadie2 = new Movie(
	    		"1",
	        "Nadie 2",
	        "Hutch regresa enfrentándose a nuevos enemigos tras sus vacaciones familiares.",
	        "Acción, Comedia, Thriller, Crimen",
	        "Bob Odenkirk",
	        "Timo Tjahjanto",
	        "/imagenes/posterNadie2.jpg",
	        "/imagenes/btnNadie2.png",
	        "https://www.youtube.com/watch?v=latAzkdZJO4&ab_channel=RoyalFilms",
	        "1h 55m"
	    );
	    
	    Movie conjuro = new Movie(
	    		"2",
	            "El Conjuro",
	            "Los Warren investigan un nuevo caso sobrenatural lleno de terror y misterio.",
	            "Terror",
	            "Vera Farmiga, Patrick Wilson",
	            "James Wan",
	            "/imagenes/posterConjuro.jpg",
	            "/imagenes/btnElConjuro.png",
	            "https://www.youtube.com/watch?v=pZGe0V7_L-Q&ab_channel=RoyalFilms",
	            "2h 05m"
	    );
	    
	    Movie fantasticos4 = new Movie("3",
	            "Los Cuatro Fantásticos",
	            "Un grupo de superhéroes con poderes extraordinarios lucha contra nuevas amenazas.",
	            "Acción, Aventura, Ciencia Ficción",
	            "Ioan Gruffudd, Jessica Alba, Chris Evans, Michael Chiklis",
	            "Matt Shakman",
	            "/imagenes/poster4Fantasticos.jpg",
	            "/imagenes/btnCuatroFantasticos.png",
	            "https://www.youtube.com/watch?v=g-a8Db2xea0&ab_channel=RoyalFilms",
	            "2h 10m"
	    );
	    
	    save(Nadie2);
	    save(conjuro);
	    save(fantasticos4);
	}

	public Movie save (Movie movie) {
		Movie encontrado = findById(movie.getId());
		if(encontrado != null) {
			return null;
		}		
		return repositorymovie.save(movie);
	}
	
	public Movie findById (String id) {
		return repositorymovie.findById(id);
	}
	
	public List<Movie> findAll(){
		return repositorymovie.findAll();
	}
	
	public Movie update(Movie movie) {
		return repositorymovie.update(movie);
	}
	
	public void deleteById(String id) {
		 repositorymovie.deleteByID(id);	 
	}
	
	public List<Movie> buscarPorFiltros(String descripcion, String clasificacion) {
        return repositorymovie.buscarPorFiltros(descripcion, clasificacion);
    }
	
	public Movie findByAuthToken (String authToken) {
		return repositorymovie.findByAuthToken(authToken);
	}
}
