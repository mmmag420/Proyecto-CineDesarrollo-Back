package com.example.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Car;

public interface RepositoryCarrito extends JpaRepository<Car, Integer>{
	
}
