package com.example.demo;

import com.example.demo.servicios.ServiceCarrito;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.demo.model.Bill;
import com.example.demo.model.Car;
import com.example.demo.model.Food;
import com.example.demo.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "*")
@Tag(name = "Carrito", description = "Operaciones para gestionar el carrito de compras")
public class ControllerCarrito {
	
	private final ServiceCarrito service;
	private final ObjectMapper mapper;

	@Autowired
    public ControllerCarrito(ServiceCarrito service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "Agregar combo", description = "Body: { \"carrito\": Car, \"combo\": Food }")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito actualizado",
                    content = @Content(schema = @Schema(implementation = Car.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (carrito/combo)")
    })
    @PostMapping("/combos")
    public ResponseEntity<?> agregarCombo(@RequestBody Map<String, Object> body) {
        Car carrito = mapper.convertValue(body.get("carrito"), Car.class);
        Food combo  = mapper.convertValue(body.get("combo"),   Food.class);
        Car actualizado = service.agregarComboAlCarrito(carrito, combo);
        return (actualizado != null)
                ? ResponseEntity.ok(actualizado)
                : ResponseEntity.badRequest().body("Datos inválidos (carrito/combo)");
    }

    @Operation(summary = "Quitar una unidad de combo", description = "Body: { \"carrito\": Car, \"combo\": Food }")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito actualizado",
                    content = @Content(schema = @Schema(implementation = Car.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (carrito/combo)")
    })
    @DeleteMapping("/combos")
    public ResponseEntity<?> quitarUnaUnidadCombo(@RequestBody Map<String, Object> body) {
        Car carrito = mapper.convertValue(body.get("carrito"), Car.class);
        Food combo  = mapper.convertValue(body.get("combo"),   Food.class);
        Car actualizado = service.quitarUnaUnidadCombo(carrito, combo);
        return (actualizado != null)
                ? ResponseEntity.ok(actualizado)
                : ResponseEntity.badRequest().body("Datos inválidos (carrito/combo)");
    }

    @Operation(summary = "Listar combos del carrito", description = "Body: Car")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Food.class))))
    @PostMapping("/combos/listar")
    public ResponseEntity<?> listarCombos(@RequestBody Car carrito) {
        return ResponseEntity.ok(service.listarCombosDelCarrito(carrito));
    }

    @Operation(summary = "Agregar entrada", description = "Body: { \"carrito\": Car, \"ticket\": Ticket }")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito actualizado",
                    content = @Content(schema = @Schema(implementation = Car.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (carrito/ticket)")
    })
    @PostMapping("/entradas")
    public ResponseEntity<?> agregarEntrada(@RequestBody Map<String, Object> body) {
        Car carrito = mapper.convertValue(body.get("carrito"), Car.class);
        Ticket t    = mapper.convertValue(body.get("ticket"),  Ticket.class);

        Car actualizado = service.agregarEntradaAlCarrito(carrito, t);
        return (actualizado != null)
                ? ResponseEntity.ok(actualizado)
                : ResponseEntity.badRequest().body("Datos inválidos (carrito/ticket)");
    }

    @Operation(summary = "Eliminar entrada", description = "Body: { \"carrito\": Car, \"ticket\": Ticket }")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito actualizado",
                    content = @Content(schema = @Schema(implementation = Car.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (carrito/ticket)")
    })
    @DeleteMapping("/entradas")
    public ResponseEntity<?> eliminarEntrada(@RequestBody Map<String, Object> body) {
        Car carrito = mapper.convertValue(body.get("carrito"), Car.class);
        Ticket t    = mapper.convertValue(body.get("ticket"),  Ticket.class);

        Car actualizado = service.eliminarEntradaDelCarrito(carrito, t);
        return (actualizado != null)
                ? ResponseEntity.ok(actualizado)
                : ResponseEntity.badRequest().body("Datos inválidos (carrito/ticket)");
    }

    @Operation(summary = "Listar entradas del carrito", description = "Body: Car")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Ticket.class))))
    @PostMapping("/entradas/listar")
    public ResponseEntity<?> listarEntradas(@RequestBody Car carrito) {
        return ResponseEntity.ok(service.listarEntradasDelCarrito(carrito));
    }

    @Operation(summary = "Vaciar carrito", description = "Body: Car")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Carrito vaciado"),
            @ApiResponse(responseCode = "400", description = "Carrito inválido")
    })
    @PostMapping("/vaciar")
    public ResponseEntity<?> vaciar(@RequestBody Car carrito) {
        if (carrito == null) return ResponseEntity.badRequest().body("Carrito inválido");
        service.vaciarCarrito(carrito);
        return ResponseEntity.noContent().build(); // 204
    }

    @Operation(summary = "Checkout (adjuntar carrito a factura)",
            description = "Body: { \"carrito\": Car, \"factura\": Bill }")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Factura con carrito adjunto",
                    content = @Content(schema = @Schema(implementation = Bill.class))),
            @ApiResponse(responseCode = "409", description = "La factura ya tiene carrito adjunto"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (carrito/factura)")
    })
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody Map<String, Object> body) {
        Car carrito = mapper.convertValue(body.get("carrito"), Car.class);
        Bill factura = mapper.convertValue(body.get("factura"), Bill.class);

        if (carrito == null || factura == null) {
            return ResponseEntity.badRequest().body("Datos inválidos (carrito/factura)");
        }
        if (factura.getCarrito() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("La factura ya tiene un carrito adjunto");
        }

        boolean ok = service.agregarCarritoEnFactura(carrito, factura);
        if (!ok) {
            return ResponseEntity.badRequest().body("No fue posible adjuntar el carrito a la factura");
        }
        return ResponseEntity.ok(factura);
    }
    

}
