package com.example.demo;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.demo.model.Hall;
import com.example.demo.model.Hall.Dia;
import io.swagger.v3.oas.annotations.responses.*;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.demo.servicios.ServiceMovie;
import com.example.demo.servicios.ServiceSala;


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
	
 
    @Operation(summary = "Cartelera por día", description = "Devuelve todas las funciones (todas las salas) del día indicado.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping("/cartelera/{dia}")
    public ResponseEntity<List<Hall>> carteleraPorDia(
            @Parameter(description = "Día de la semana (LUNES..DOMINGO)", example = "LUNES")
            @PathVariable Dia dia) {
        List<Hall> out = serviceSala.listarTodas().stream()
                .filter(h -> h.getDiaPelicula() == dia)
                .sorted(Comparator.comparing(Hall::getNumSala).thenComparing(Hall::getHoraInicio))
                .collect(Collectors.toList());
        return ResponseEntity.ok(out);
    }

    
    @Operation(summary = "Funciones de una sala por día", description = "Devuelve las funciones de la sala indicada filtradas por día.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping("/{idSala}/funciones")
    public ResponseEntity<List<Hall>> funcionesPorSalaYDia(
            @Parameter(description = "Número de sala", example = "1") @PathVariable int idSala,
            @Parameter(description = "Día de la semana", example = "MARTES") @RequestParam Dia dia) {
        List<Hall> out = serviceSala.listarPorSalaYDia(idSala, dia).stream()
                .sorted(Comparator.comparing(Hall::getHoraInicio))
                .collect(Collectors.toList());
        return ResponseEntity.ok(out);
    }

    
    @Operation(summary = "Obtener función", description = "Consulta una función por sala + día + hora de inicio.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Función encontrada"),
        @ApiResponse(responseCode = "404", description = "Función no encontrada")
    })
    @GetMapping("/funcion")
    public ResponseEntity<?> obtenerFuncion(
            @Parameter(description = "Número de sala", example = "1") @RequestParam int sala,
            @Parameter(description = "Día de la semana", example = "VIERNES") @RequestParam Dia dia,
            @Parameter(description = "Hora de inicio (HH:mm)", example = "16:50")
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime horaInicio) {
        Hall h = serviceSala.buscarPorDiaYHora(sala, dia, horaInicio);
        return (h == null) ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("Función no encontrada") : ResponseEntity.ok(h);
    }

    
    @Operation(summary = "Estado de sillas", description = "Devuelve el estado de todas las sillas (true=ocupada, false=libre) para una función.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado obtenido"),
        @ApiResponse(responseCode = "404", description = "Función no encontrada")
    })
    @GetMapping("/funcion/sillas")
    public ResponseEntity<?> estadoSillas(
            @Parameter(description = "Número de sala", example = "1") @RequestParam int sala,
            @Parameter(description = "Día de la semana", example = "SABADO") @RequestParam Dia dia,
            @Parameter(description = "Hora de inicio (HH:mm)", example = "21:30")
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime horaInicio) {
        Hall h = serviceSala.buscarPorDiaYHora(sala, dia, horaInicio);
        if (h == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Función no encontrada");
        boolean[] estado = serviceSala.estadoSillas(h);
        return ResponseEntity.ok(estado);
    }

    
    @Operation(summary = "Reservar sillas", description = "Reserva una o varias sillas para una función. Falla si alguna silla ya está ocupada.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reserva exitosa"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "404", description = "Función no encontrada"),
        @ApiResponse(responseCode = "409", description = "Alguna silla no existe o ya está ocupada")
    })
    @PostMapping("/funcion/reservar")
    public ResponseEntity<?> reservar(@RequestBody Map<String, Object> body) {
        try {
            int sala = (Integer) body.get("sala");
            Dia dia = Dia.valueOf(((String) body.get("dia")).toUpperCase());
            LocalTime horaInicio = LocalTime.parse((String) body.get("horaInicio"));
            @SuppressWarnings("unchecked")
            List<Integer> asientos = (List<Integer>) body.get("asientos");

            if (asientos == null || asientos.isEmpty()) {
                return ResponseEntity.badRequest().body("Debes enviar 'asientos'");
            }

            Hall h = serviceSala.buscarPorDiaYHora(sala, dia, horaInicio);
            if (h == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Función no encontrada");

            for (int a : asientos) {
                boolean ok = serviceSala.reservarSilla(h, a);
                if (!ok) {
                    for (int b : asientos) {
                        if (b == a) break;
                        serviceSala.cancelarSilla(h, b);
                    }
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Alguna silla no existe o ya está ocupada");
                }
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Formato inválido del cuerpo: " + e.getMessage());
        }
    }
    

    @Operation(summary = "Cancelar silla", description = "Libera una silla previamente reservada en una función.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cancelación exitosa"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "404", description = "Función no encontrada"),
        @ApiResponse(responseCode = "409", description = "La silla no existe o ya estaba libre")
    })
    @PostMapping("/funcion/cancelar")
    public ResponseEntity<?> cancelar(@RequestBody Map<String, Object> body) {
        try {
            int sala = (Integer) body.get("sala");
            Dia dia = Dia.valueOf(((String) body.get("dia")).toUpperCase());
            LocalTime horaInicio = LocalTime.parse((String) body.get("horaInicio"));
            int asiento = (Integer) body.get("asiento");

            Hall h = serviceSala.buscarPorDiaYHora(sala, dia, horaInicio);
            if (h == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Función no encontrada");

            boolean ok = serviceSala.cancelarSilla(h, asiento);
            if (!ok) return ResponseEntity.status(HttpStatus.CONFLICT).body("La silla no existe o ya estaba libre");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Formato inválido del cuerpo: " + e.getMessage());
        }
    }

    
    @Operation(
            summary = "Limpiar sala por ID",
            description = "Vacía todas las sillas de las salas cuyo ID coincida con el proporcionado, "
                        + "independientemente de si la función está vencida o no."
        )
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Limpieza ejecutada"),
            @ApiResponse(responseCode = "404", description = "No se encontraron salas con el ID indicado"),
            @ApiResponse(responseCode = "400", description = "Parámetro inválido")
        })
        @PostMapping("/salas/{id}/limpiar")
        public ResponseEntity<?> limpiarSalaPorId(
            @Parameter(description = "ID de la sala a limpiar", required = true, example = "1")
            @PathVariable("id") Integer idSala
        ) {
            try {
                if (idSala == null) {
                    return ResponseEntity.badRequest().body("Parámetro 'id' inválido o ausente.");
                }

                int salasLimpias = serviceSala.limpiarSalaPorId(idSala);

                if (salasLimpias == 0) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("No se encontraron salas con el ID " + idSala + ".");
                }

                return ResponseEntity.ok(
                    java.util.Map.of(
                        "idSala", idSala,
                        "salasLimpias", salasLimpias,
                        "mensaje", "Se vaciaron todas las sillas de las salas con ID " + idSala + "."
                    )
                );
            } catch (Exception e) {
                return ResponseEntity.badRequest()
                        .body("Error al procesar la solicitud: " + e.getMessage());
            }
        }
    
    
    @PostMapping("/crear-por-defecto/auto")
    @Operation(summary = "Crear funciones por defecto para una película",
               description = "Crea funciones 16:50 y 21:30 para todos los días en una sala nueva (next numSala).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Funciones creadas"),
        @ApiResponse(responseCode = "404", description = "Película no encontrada o no se crearon funciones")
    })
    public ResponseEntity<Map<String, Object>> crearFuncionesPorDefectoAuto(
            @RequestParam Integer movieId,
            @RequestParam(required = false, defaultValue = "39") Integer capacidad) {

        if (movieId == null || movieId <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "movieId inválido"));
        }
        if (capacidad == null || capacidad <= 0) capacidad = 39;

        System.out.println("[SALAS] crear-por-defecto/auto movieId=" + movieId + " cap=" + capacidad);

        int creadas = serviceSala.crearFuncionesPorDefectoParaMovieAutoSala(movieId, capacidad);
        if (creadas == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Película no encontrada o no se crearon funciones"));
        }
        return ResponseEntity.ok(Map.of(
                "movieId", movieId,
                "capacidad", capacidad,
                "funcionesCreadas", creadas
        ));
    }
	
}
