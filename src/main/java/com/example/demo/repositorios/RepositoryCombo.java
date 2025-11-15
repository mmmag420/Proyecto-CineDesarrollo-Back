package com.example.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Food;

public interface RepositoryCombo extends JpaRepository<Food, Integer> {
	
}
