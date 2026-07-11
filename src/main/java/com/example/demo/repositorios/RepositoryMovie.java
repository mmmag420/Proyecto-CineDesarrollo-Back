package com.example.demo.repositorios;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Movie;

public interface RepositoryMovie extends JpaRepository<Movie, Integer> {

	Optional<Movie> findById(int id);
}
