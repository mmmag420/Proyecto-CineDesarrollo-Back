package com.example.demo.servicios;

import com.example.demo.model.Food;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.repositorios.RepositoryCombo;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ServiceCombo {
	
	private final RepositoryCombo repo;

    public ServiceCombo(RepositoryCombo repo) {
    	this.repo = repo;

    }
    
    
    @PostConstruct
    public void iniciarCombosQuemados() {
    	
    	seedIfAbsent(new Food(1, 43000, "Combo 1: Crispetas + 2 bebidas + 2 chocolates Jet"));
    	seedIfAbsent(new Food(2, 62000, "Combo 2: 2 bebidas + perro caliente + crispetas"));
    	seedIfAbsent(new Food(3, 27000, "Combo 3: Crispetas + bebida + Jet"));
    	seedIfAbsent(new Food(4, 26000, "Combo 4: Bebida + perro + Jet"));
    	seedIfAbsent(new Food(5, 50000, "Combo 5: Crispetas + bebida + perro"));
    	seedIfAbsent(new Food(6, 20000, "Combo Junior: Crispetas + bebida + perro pequeño"));
    	
    }

    
    public List<Food> listarCombos() {
        return repo.findAll();
    }


    public Food buscarCombo(int id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Combo no encontrado"));
    }


    public void agregarCombo(Food food) {
        if (repo.existsById(food.getIdCombo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El combo ya existe");
        }
        repo.save(food);
    }
    
    private void seedIfAbsent(Food f) {
        if (!repo.existsById(f.getIdCombo())) {
            repo.save(f);
        }
    }

}
