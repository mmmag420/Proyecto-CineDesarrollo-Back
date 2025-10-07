package com.example.demo;

import model.Food;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class ServiceCombo {
	
	
	private final RepositoryCombo repo;

    public ServiceCombo(RepositoryCombo repo) {
    	this.repo = repo;
    	iniciarCombosQuemados();

    }
    
    //inicia combos quemados
    public void iniciarCombosQuemados() {
    	Food combo1 = new Food(1, 43000, "Combo 1: Crispetas + 2 bebidas + 2 chocolates Jet");
    	Food combo2 = new Food(2, 62000, "Combo 2: 2 bebidas + perro caliente + crispetas");
    	Food combo3 = new Food(3, 27000, "Combo 3: Crispetas + bebida + Jet");
    	Food combo4 = new Food(4, 26000, "Combo 4: Bebida + perro + Jet");
    	Food combo5 = new Food(5, 50000, "Combo 5: Crispetas + bebida + perro");
    	Food combo6 = new Food(6, 20000, "Combo Junior: Crispetas + bebida + perro pequeño");
    	
    	agregarCombo(combo1);
    	agregarCombo(combo2);
    	agregarCombo(combo3);
    	agregarCombo(combo4);
    	agregarCombo(combo5);
    	agregarCombo(combo6);
    
    }

    //devuelve todos los combos disponiles
    public List<Food> listarCombos() {
        return repo.findAll();
    }

    //busca combo por el id
    public Food buscarCombo(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Combo no encontrado"));
    }

    //agrega un nuevo combo si no existe
    public void agregarCombo(Food food) {
        if (repo.existsById(food.getIdCombo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El combo ya existe");
        }
        repo.save(food);
    }

}
