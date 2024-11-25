package ec.edu.ucuenca.vehiculo.ws.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;


/**
 * Implementa la clase Application mediante herencia para habilitar servicios
 * REST en la aplicación, en el path dado.
 * 
 * @author Dustin Ghia
 * 
 */
@ApplicationPath("/rest")
public class RestApplication extends Application {
}
