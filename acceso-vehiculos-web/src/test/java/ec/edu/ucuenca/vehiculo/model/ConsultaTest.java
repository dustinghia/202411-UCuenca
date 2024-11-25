package ec.edu.ucuenca.vehiculo.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConsultaTest extends BaseTest {
	
	private Bitacora bitacora;
	
	@BeforeEach
	public void init() {
		bitacora = new Bitacora();
		bitacora.registroDAO = this.registroEntradaSalidaDAO;
	}
	
	@Test
	public void consultarVehiculosTest() throws BitacoraException {
		
		final String placa01 = "AVF-2121";
		final String placa02 = "AVF-2122";
		final String placa03 = "AVF-2123";
		final String placa04 = "AVF-2124";

		final String fechaIngreso01 = "07-11-2024";
		final String horaIngreso01_01 = "09:12:30";
		final String horaIngreso01_02 = "09:13:30";
		final String horaIngreso01_03 = "09:14:30";
		final String horaIngreso01_04 = "09:15:30";

		final String horaSalida01_02 = "10:13:30";
		
		final String fechaIngreso02 = "08-11-2024";
		final String horaIngreso02_02 = "10:00:30";
		final String horaSalida02_02 = "11:30:30";

		// Ingresando vehículos el día 1
		bitacora.registrarIngreso(placa01, fechaIngreso01, horaIngreso01_01);
		bitacora.registrarIngreso(placa02, fechaIngreso01, horaIngreso01_02);
		bitacora.registrarIngreso(placa03, fechaIngreso01, horaIngreso01_03);
		bitacora.registrarIngreso(placa04, fechaIngreso01, horaIngreso01_04);
		
		// Registrando una salida el día 1
		bitacora.registrarSalida(placa02, fechaIngreso01, horaSalida01_02);
		
		// Ingresando 1 vehículo el día 2 (el único que salió)
		bitacora.registrarIngreso(placa02, fechaIngreso02, horaIngreso02_02);
		
		// Registrando una salida el día 2
		bitacora.registrarSalida(placa02, fechaIngreso02, horaSalida02_02);
		
		// Consultando todos
		List<RegistroEntradaSalida> todos = bitacora.recuperarTodos();
		
		// Consultando una placa en particular
		List<RegistroEntradaSalida> registrosDeDeterminadaPlaca = bitacora.recuperarRegistrosPorPlaca(placa02);

		System.out.println("Todos los registros:");
		System.out.println(todos);
		assertThat(todos).hasSize(5);
		System.out.println("Los registros de la placa determinada");
		System.out.println(registrosDeDeterminadaPlaca);
		assertThat(registrosDeDeterminadaPlaca).hasSize(2);
		assertThat(registrosDeDeterminadaPlaca.get(0).getPlaca()).isEqualTo(placa02);
		assertThat(registrosDeDeterminadaPlaca.get(0).getIngreso().toString()).isEqualTo("2024-11-08T10:00:30");
		assertThat(registrosDeDeterminadaPlaca.get(0).getSalida().toString()).isEqualTo("2024-11-08T11:30:30");
		assertThat(registrosDeDeterminadaPlaca.get(1).getPlaca()).isEqualTo(placa02);
		assertThat(registrosDeDeterminadaPlaca.get(1).getIngreso().toString()).isEqualTo("2024-11-07T09:13:30");
		assertThat(registrosDeDeterminadaPlaca.get(1).getSalida().toString()).isEqualTo("2024-11-07T10:13:30");
	}

}
