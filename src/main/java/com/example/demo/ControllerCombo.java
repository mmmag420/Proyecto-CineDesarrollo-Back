package com.example.demo;

import model.Food;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;


@RestController
@RequestMapping("/api/foods")
@Tag(name = "API de Combos", description = "controlador REST para la gestion de combos en el cine")
public class ControllerCombo {
	
	private final ServiceCombo service;

    public ControllerCombo(ServiceCombo service) {
		this.service = service;
	}

    @Operation(summary = "listar todos los combos", description = "devuelve la lista de combos disponibles")
    @ApiResponse(responseCode = "200", description = "lista obtenida correctamente")
    @GetMapping
    public List<Food> getAll() {
        return service.listarCombos();
    }

    @Operation(summary = "buscar combo por id", description = "devuelve un combo especifico segun su id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "combo encontrado"),
        @ApiResponse(responseCode = "404", description = "no existe un combo con ese id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(
    		@Parameter(description = "id del combo", example = "1") 
    		@PathVariable int id) {
    	Food combo = service.buscarCombo(id);
        return (combo == null) ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("combo no encontrado") : ResponseEntity.ok(combo);   
    }

    @Operation(summary = "crear un nuevo combo", description = "agrega un combo al catalogo")
    @ApiResponse(responseCode = "201", description = "combo creado exitosamente")
    @PostMapping
    public void create(
    		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "objeto combo con id, precio y descripcion")
    		@RequestBody Food food) {
        service.agregarCombo(food);
    }

}
