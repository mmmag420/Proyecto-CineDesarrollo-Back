package com.example.demo;

import java.util.List;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


import model.Client;


@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*") 
@Tag(name="Clientes", description="Gestión de clientes")
public class ControllerClient {

	private final ServiceClient clienteService;

	@Autowired
	public ControllerClient(ServiceClient clienteService) {
		this.clienteService = clienteService;
	}
	
	
    // listar
	@Operation(summary = "Listar clientes", description = "Devuelve todos los clientes registrados.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")	
	@GetMapping
	public ResponseEntity<List<Client>> listar() {
	        return ResponseEntity.ok(clienteService.listarClientes());
	    }

	
	
	//guardar cliente
    @Operation(summary = "Crear cliente", description = "Registra un nuevo cliente. Reglas: cédula única y edad >= 18.")
    @ApiResponses({ @ApiResponse(responseCode = "201", description = "Cliente creado"),
    				@ApiResponse(responseCode = "409", description = "Cédula ya registrada"), 
    				@ApiResponse(responseCode = "400", description = "Datos inválidos (cédula/edad)")})	
	@PostMapping
	public ResponseEntity<?> crear(@RequestBody Client body) {
		boolean ok = clienteService.guardarCliente(body);
		if(ok) {
			return ResponseEntity.status(HttpStatus.CREATED).body(body); //201
		}
		
		Client existente = clienteService.buscarCliente(body.getCedula());
		if(existente != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La cédula ya está registrada"); // 409			
		}
        return ResponseEntity.badRequest().body("Datos inválidos (cedula/edad)"); // 400
		
	}
    
	
	//buscar
    @Operation(summary = "Obtener cliente por cédula", description = "Consulta un cliente específico usando su cédula.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")})  
    
	@GetMapping("/{cedula}")
	public ResponseEntity<?> obtener(@Parameter(description = "Cédula del cliente a consultar", example = "1034290939") @PathVariable String cedula) {
		Client cliente = clienteService.buscarCliente(cedula);
        return (cliente == null) ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado") : ResponseEntity.ok(cliente);   
	}
	
	//editar
    @Operation(summary = "Actualizar cliente (PUT)", description = "Reemplaza todos los datos del cliente manteniendo la misma cédula.")    
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente actualizado"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos (edad >= 18)")
    })
	@PutMapping("/{cedula}")
	public ResponseEntity<?> reemplazar(@Parameter(description = "Cédula del cliente a actualizar", example = "1034290939")@PathVariable String cedula,@RequestBody Client body) {
	
    	body.setCedula(cedula);
		if(clienteService.buscarCliente(cedula) == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
		}
		
		boolean ok = clienteService.editarCliente(body);
		if(!ok) {
			return ResponseEntity.badRequest().body("Datos inválidos (edad >= 18)"); // 400
		}
		return ResponseEntity.ok(body);
	}
	
	//eliminar
    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente por su cédula.")   
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cliente eliminado"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    
	@DeleteMapping("/{cedula}")
	public ResponseEntity<?> eliminar(@Parameter(description = "Cédula del cliente a eliminar", example = "1034290939")@PathVariable String cedula) {
		Client existente = clienteService.buscarCliente(cedula);
		if(existente == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado"); // 404
        }
		boolean ok = clienteService.eliminarCliente(existente);
		if(!ok) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo eliminar"); // fallback
		}
		return ResponseEntity.noContent().build();

	}
	
	
	
	


	
	
}
