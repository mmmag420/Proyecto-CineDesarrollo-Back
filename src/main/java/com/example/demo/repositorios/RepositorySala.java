package com.example.demo.repositorios;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.demo.model.Hall;


public interface RepositorySala extends JpaRepository<Hall, Integer> {
    Optional<Hall> findByNumSala(int numSala);
    Optional<Hall> findByNumSalaAndDiaPeliculaAndHoraInicio(int numSala, Hall.Dia diaPelicula, LocalTime horaInicio);
    Optional<Hall> findByNumSalaAndDiaPelicula(int numSala, Hall.Dia diaPelicula);
    List<Hall> findByMovie_Id(String movieId);
}
