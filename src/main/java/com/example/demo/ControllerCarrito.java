package com.example.demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import model.Car;
import model.Food;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
@Tag(name = "Carrito", description = "Operaciones para gestionar el carrito de compras")
public class ControllerCarrito {
	
	private final ServiceCarrito servicio;

    public ControllerCarrito(ServiceCarrito servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Obtener carrito", description = "Devuelve el carrito con su precio total calculado")
    @ApiResponse(responseCode = "200", description = "Carrito obtenido correctamente")
    @GetMapping
    public Car obtenerCarrito() {
        return servicio.obtenerCarrito();
    }

    @Operation(summary = "Listar combos del carrito", description = "Devuelve los combos actualmente en el carrito")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping("/combos")
    public List<Food> listarCombos() {
        return servicio.listarCombosDelCarrito();
    }

    @Operation(summary = "Agregar combo", description = "Agrega un combo al carrito por su id")
    @ApiResponse(responseCode = "201", description = "Combo agregado correctamente")
    @PostMapping("/combos/{idCombo}")
    public void agregarCombo(@PathVariable int idCombo) {
        servicio.agregarComboAlCarrito(idCombo);
    }

    @Operation(summary = "Eliminar combo", description = "Elimina todas las ocurrencias de un combo por id")
    @ApiResponse(responseCode = "204", description = "Combo eliminado correctamente")
    @DeleteMapping("/combos/{idCombo}")
    public void eliminarCombo(@PathVariable int idCombo) {
        servicio.eliminarComboDelCarrito(idCombo);
    }

    @Operation(summary = "Vaciar carrito", description = "Elimina todos los combos del carrito")
    @ApiResponse(responseCode = "204", description = "Carrito vaciado correctamente")
    @DeleteMapping
    public void vaciar() {
        servicio.vaciarCarrito();
    }

}
