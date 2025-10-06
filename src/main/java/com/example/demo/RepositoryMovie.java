package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import model.Movie;

@Repository
public class RepositoryMovie {

	private final List<Movie> baseDeDatos = new ArrayList<>();
	private final List<String> authTokens = new ArrayList<>();
	
	public Movie save(Movie movie) {
		baseDeDatos.add(movie);
		authTokens.add(movie.getNombre());
		return movie;
	}
	
	public Movie findById(String id) {
		for(Movie movie : baseDeDatos) {
			if(movie.getId().equals(id)) {
				return movie;
			}
		}
		return null;
	}
	
	public List<Movie> findAll(){
		return new ArrayList<>(baseDeDatos);
	}
	
	public void deleteByID(String id) {
		for(int i=0; i<baseDeDatos.size(); i++) {
			if(baseDeDatos.get(i).getId().equals(id)) {
				baseDeDatos.remove(i);
				return;
			}
		}
	}
	
	public Movie update(Movie movie) {
		for(int i = 0; i<baseDeDatos.size(); i++) {
			if(baseDeDatos.get(i).getId().equals(movie.getId())) {
				baseDeDatos.set(i, movie);
				return movie;
			}
		}
		return null;
	}
	
    public List<Movie> buscarPorFiltros(String descripcion, String clasificacion){
    	List<Movie> resultado = new ArrayList<>();
    	for(Movie movie : baseDeDatos) {
    		boolean coincideDescripcion = (descripcion == null || movie.getDescripcion().contains(descripcion));
    		boolean coincideClasificacion = (clasificacion == null || movie.getClasificacion().contains(clasificacion));
    		if(coincideDescripcion && coincideClasificacion) {
    			resultado.add(movie);
    		}
    	}
    	return resultado;
    }
    
    public Movie findByAuthToken(String authToken) {
        for (String token : authTokens) {
            if (token.equals(authToken)) {
            	for (Movie movie : baseDeDatos) {
                    if (movie.getId().equals(authToken)) {
                        return movie;
                    }
                }
            }
        }
        return null;
    }   
}
