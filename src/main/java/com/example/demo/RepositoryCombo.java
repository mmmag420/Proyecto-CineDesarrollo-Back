package com.example.demo;

import model.Food;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RepositoryCombo {
	
	private final List<Food> foods = new ArrayList<>();
	
	//obtiene todos los combos registrados
	public List<Food> findAll() {
        List<Food> copia = new ArrayList<>();
        for (int i = 0; i < foods.size(); i++) {
            copia.add(foods.get(i));
        }
        return copia;
    }

	//busca un combo por su id
    public Optional<Food> findById(int id) {
        for (int i = 0; i < foods.size(); i++) {
            if (foods.get(i).getIdCombo() == id) {
                return Optional.of(foods.get(i));
            }
        }
        return Optional.empty();
    }

    //verifica si el combo existe
    public boolean existsById(int id) {
        for (int i = 0; i < foods.size(); i++) {
            if (foods.get(i).getIdCombo() == id) return true;
        }
        return false;
    }

    //guarda un combo en la lista
    public void save(Food food) {
        foods.add(food);
    }


}
