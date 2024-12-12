package ec.edu.ucuenca.vehiculo.service;


import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ec.edu.ucuenca.vehiculo.model.Bitacora;
import ec.edu.ucuenca.vehiculo.model.BitacoraException;
import ec.edu.ucuenca.vehiculo.model.RegistroDTO;
import ec.edu.ucuenca.vehiculo.model.RegistroEntradaSalida;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/vehiculos")
public class BitacoraService {
	
	private static final Logger LOGGER = LogManager.getLogger(BitacoraService.class);
	
	@EJB
	private Bitacora bitacora;
	
	@POST
	@Path("/ingreso")
	@Produces({ MediaType.APPLICATION_JSON})
	@Consumes({ MediaType.APPLICATION_JSON})
	public Response registrarIngreso(RegistroDTO registroIngreso) {
		try {
			LOGGER.info("Registrando ingreso: " + registroIngreso);
			bitacora.registrarIngreso(registroIngreso.getPlaca(), registroIngreso.getFecha(), registroIngreso.getHora());
			return Response.status(Response.Status.CREATED) // Creado sin errores
                    .entity(Bitacora.MSG_INGRESO_REGISTRADO_EXITOSAMENTE)
                    .build();
		} catch (BitacoraException e) {
			LOGGER.error(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST) // 400 Bad Request
                    .entity(e.getMessage())
                    .build();
		}
	}
	
	@POST
	@Path("/salida")
	@Produces({ MediaType.APPLICATION_JSON})
	@Consumes({ MediaType.APPLICATION_JSON})
	public Response registrarSalida(RegistroDTO registroSalida) {
		try {
			LOGGER.info("Registrando salida: " + registroSalida);
			bitacora.registrarSalida(registroSalida.getPlaca(), registroSalida.getFecha(), registroSalida.getHora());
			return Response.status(Response.Status.OK) // Aceptado sin errores
                    .entity(Bitacora.MSG_SALIDA_REGISTRADO_EXITOSAMENTE)
                    .build();
		} catch (BitacoraException e) {
			LOGGER.error(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST) // 400 Bad Request
                    .entity(e.getMessage())
                    .build();
		}
	}
	
	@GET
	@Path("/todos")
	@Produces({ MediaType.APPLICATION_JSON})
	public Response recuperarTodos() {
		LOGGER.info("Consultando todos los vehiculos... ");
		
		List<RegistroEntradaSalida> todosRegistros = bitacora.recuperarTodos();

		ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
		
		try {
			String result = om.writeValueAsString(todosRegistros);
			
			return Response.status(Response.Status.OK) // 200 OK
                    .entity(result)
                    .build();
		
		} catch (JsonProcessingException e) {
			LOGGER.error(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST) // 400 Bad Request
                    .entity(e.getMessage())
                    .build();
		}
	}
	
	@GET
	@Path("/activos")
	@Produces({ MediaType.APPLICATION_JSON})
	public Response recuperarActivos() {
		LOGGER.info("Consultando vehiculos activos... ");
		
		List<RegistroEntradaSalida> vehiculosActivos = bitacora.obtenerRegistrosAbiertos();

		ObjectMapper om = new ObjectMapper();
		om.registerModule(new JavaTimeModule());
		
		try {
			String result = om.writeValueAsString(vehiculosActivos);
			
			return Response.status(Response.Status.OK) // 200 OK
					.entity(result)
					.build();
		
		} catch (JsonProcessingException e) {
			LOGGER.error(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST) // 400 Bad Request
					.entity(e.getMessage())
					.build();
		}
	}

	//recuperar registros de una placa entre dos fechas
	@POST
	@Path("/placa")
	@Produces({ MediaType.APPLICATION_JSON})
	@Consumes({ MediaType.APPLICATION_JSON})
	public Response recuperarPorPlaca(String placa, String fechaInicio, String fechaFin) {
		LOGGER.info("Consultando vehiculos por placa... ");


		
		try {
			List<RegistroEntradaSalida> vehiculosPorPlaca = bitacora.obtenerRegistrosEntreFechas(fechaInicio, fechaFin, placa);
	
			ObjectMapper om = new ObjectMapper();
	
			om.registerModule(new JavaTimeModule());
			String result = om.writeValueAsString(vehiculosPorPlaca);

			return Response.status(Response.Status.OK) // 200 OK
					.entity(result)
					.build();

		} catch (JsonProcessingException e) {
			LOGGER.error(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST) // 400 Bad Request
					.entity(e.getMessage())
					.build();
		} catch (BitacoraException e) {
			LOGGER.error(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST) // 400 Bad Request
					.entity(e.getMessage())
					.build();
		}

	}

	
}
