package com.example.demo;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import model.Hall;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;


@RestController
@RequestMapping("/api/salas")
@CrossOrigin(origins = "*")
@Tag(name = "Salas", description = "Cartelera, funciones y reservas por sala")
public class ControllerSala {

    private final ServiceSala serviceSala;
    private final ServiceMovie serviceMovie;
    
    @Autowired
	public ControllerSala(ServiceSala serviceSala, ServiceMovie serviceMovie) {
		this.serviceSala = serviceSala;
		this.serviceMovie = serviceMovie;
	}
	
	 @Operation(summary = "Sembrar semana (opcional)", description = "Crea la grilla de funciones de 7 días para 3 salas fijas usando las películas ya cargadas en ServiceMovie (ids 1,2,3).")
	 @ApiResponses({
	     @ApiResponse(responseCode = "200", description = "Semana sembrada"),
	     @ApiResponse(responseCode = "404", description = "Alguna película id=1|2|3 no existe")
	 })
	 @PostMapping("/sembrar")
    public ResponseEntity<?> sembrarSemana() {
        var m1 = serviceMovie.findById("1");
        var m2 = serviceMovie.findById("2");
        var m3 = serviceMovie.findById("3");
        if (m1 == null || m2 == null || m3 == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Falta alguna película id=1|2|3 en ServiceMovie");
        }
        serviceSala.sembrarSemana(m1, m2, m3);
        return ResponseEntity.ok("Semana sembrada");
    }
	 

	    @Operation(summary = "Listar funciones por sala y día", description = "Retorna la lista de funciones (Hall) para una sala en un día (ISO yyyy-MM-dd).")
	    @ApiResponses({
	        @ApiResponse(responseCode = "200", description = "Listado ok")
	    })
	    @GetMapping("/{sala}/funciones")
	    public ResponseEntity<List<Hall>> listarFunciones(
	            @PathVariable int sala,
	            @io.swagger.v3.oas.annotations.Parameter(description = "Día en formato ISO, ej: 2025-10-07", example = "2025-10-07")
	            @RequestParam String dia) {

	        return ResponseEntity.ok(serviceSala.listarFunciones(sala, dia));
	    }
	    
	    @Operation(summary = "Detalle de una función",
	               description = "Obtiene la función (Hall) por sala, día y hora de inicio.")
	    @ApiResponses({
	        @ApiResponse(responseCode = "200", description = "Función encontrada"),
	        @ApiResponse(responseCode = "404", description = "No existe la función")
	    })
	    @GetMapping("/{sala}/funciones/{dia}/{hora}")
	    public ResponseEntity<?> detalleFuncion(
	            @PathVariable int sala,
	            @PathVariable String dia,
	            @PathVariable String hora) {

	        var hall = serviceSala.detalleFuncion(sala, dia, hora);
	        if (hall == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Función no encontrada");
	        return ResponseEntity.ok(hall);
	    }
	    

	    
	    @Operation(summary = "Ocupación de sillas", description = "Devuelve un arreglo boolean[] con la ocupación de sillas para la función.")
	    @ApiResponses({
	        @ApiResponse(responseCode = "200", description = "Ocupación retornada"),
	        @ApiResponse(responseCode = "404", description = "No existe la función")
	    })
	    @GetMapping("/{sala}/ocupacion")
	    public ResponseEntity<?> ocupacion(
	            @io.swagger.v3.oas.annotations.Parameter(description = "Número de sala", example = "1")
	            @PathVariable int sala,
	            @io.swagger.v3.oas.annotations.Parameter(description = "Día ISO", example = "2025-10-07")
	            @RequestParam String dia,
	            @io.swagger.v3.oas.annotations.Parameter(description = "Hora inicio HH:mm", example = "16:50")
	            @RequestParam String hora) {

	        var snapshot = serviceSala.verSiEstaOcupada(sala, dia, hora);
	        if (snapshot == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Función no encontrada");
	        return ResponseEntity.ok(snapshot);
	    }
	    
	    @PostMapping("/{sala}/reservas")
	    @Operation(summary = "Reservar una silla", description = "Reserva una silla (numSilla) en una función (sala, día, hora).")
	    @ApiResponses({
	        @ApiResponse(responseCode = "201", description = "Reserva creada"),
	        @ApiResponse(responseCode = "404", description = "Función no encontrada"),
	        @ApiResponse(responseCode = "409", description = "Silla ya ocupada"),
	        @ApiResponse(responseCode = "400", description = "Datos inválidos")
	    })
	    
	    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"dia\": \"2025-10-07\", \"hora\": \"16:50\", \"silla\": 30 }")))	    
	    public ResponseEntity<?> reservar(
	            @PathVariable int sala,
	            @org.springframework.web.bind.annotation.RequestBody Map<String, Object> body // <-- ESTA es la importante
	    ) {
	        String dia = (String) body.get("dia");
	        String hora = (String) body.get("hora");
	        Integer silla = (body.get("silla") instanceof Integer) ? (Integer) body.get("silla") : null;

	        if (dia == null || hora == null || silla == null) {
	            return ResponseEntity.badRequest().body("Faltan campos: dia, hora, silla");
	        }

	        if (serviceSala.detalleFuncion(sala, dia, hora) == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Función no encontrada");
	        }

	        boolean ok = serviceSala.reservarSilla(sala, dia, hora, silla);
	        if (!ok) {
	            return ResponseEntity.status(HttpStatus.CONFLICT).body("Silla ya ocupada o inválida");
	        }

	        return ResponseEntity.status(HttpStatus.CREATED).build();
	    }
	    
	    
		    @Operation(summary = "Funciones por película y día",
		            description = "Devuelve funciones (sala + horarios) para la película indicada en el día dado.")
		 @ApiResponses(@ApiResponse(responseCode = "200", description = "OK"))
		 @GetMapping("/funciones-por-pelicula")
		 public ResponseEntity<List<Hall>> funcionesPorPelicula(
		         @RequestParam String peliculaId,
		         @RequestParam String dia) {
		     return ResponseEntity.ok(serviceSala.funcionesPorPelicula(peliculaId, dia));
		 }
	
}
