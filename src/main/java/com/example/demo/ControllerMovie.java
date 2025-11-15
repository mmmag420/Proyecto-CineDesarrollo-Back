package com.example.demo;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.servicios.ServiceMovie;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.demo.model.Movie;

@RestController
@RequestMapping("/api/movies")
@Tag(name = "Movies", description = "Operaciones relacionadas con películas")
public class ControllerMovie {

    private final ServiceMovie serviceMovie;

    @Autowired
    public ControllerMovie(ServiceMovie serviceMovie) {
        this.serviceMovie = serviceMovie;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las movies", description = "Lista de películas obtenidas con éxito")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de movies obtenidas con éxito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = serviceMovie.findAll();
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener movie por ID", description = "Devuelve una movie específica basado en su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Película encontrada"),
        @ApiResponse(responseCode = "404", description = "Película no encontrada")
    })
    public ResponseEntity<Movie> getMovieById(@PathVariable @Parameter(description = "ID de la película") int id) {
        Movie movie = serviceMovie.findById(id);
        if (movie != null) {
            return new ResponseEntity<>(movie, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @Operation(summary = "Crear una nueva movie", description = "Guarda una nueva película en la base de datos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Película creada con éxito")
    })
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        Movie saved = serviceMovie.save(movie);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una movie", description = "Actualiza los datos de una película existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Película actualizada con éxito"),
        @ApiResponse(responseCode = "404", description = "Película no encontrada")
    })
    public ResponseEntity<Movie> updateMovie(@PathVariable int id, @RequestBody Movie movie) {
        movie.setId(id);
        Movie updated = serviceMovie.update(movie);
        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una movie", description = "Elimina una película existente de la base de datos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Película eliminada con éxito"),
        @ApiResponse(responseCode = "404", description = "Película no encontrada")
    })
    public ResponseEntity<Void> deleteMovie(@PathVariable int id) {
        Movie movie = serviceMovie.findById(id);
        if (movie != null) {
            serviceMovie.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

