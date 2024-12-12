package ec.edu.ucuenca.vehiculo.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

	@Test
	public void consultaRegistrosAbiertosSize_Ok() throws BitacoraException {

		final String placa01 = "AVF-2121";
		final String placa02 = "AVF-2122";
		final String placa03 = "AVF-2123";
		final String placa04 = "AVF-2124";

		final String fechaIngreso01 = "07-11-2024";
		final String horaIngreso01_01 = "09:12:30";
		final String horaIngreso01_02 = "09:13:30";
		final String horaIngreso01_03 = "09:14:30";
		final String horaIngreso01_04 = "09:15:30";

		// Ingresando vehículos el día 1
		bitacora.registrarIngreso(placa01, fechaIngreso01, horaIngreso01_01);
		bitacora.registrarIngreso(placa02, fechaIngreso01, horaIngreso01_02);
		bitacora.registrarIngreso(placa03, fechaIngreso01, horaIngreso01_03);
		bitacora.registrarIngreso(placa04, fechaIngreso01, horaIngreso01_04);

		assertEquals(bitacora.obtenerRegistrosAbiertos().size(), 4);
	}

	@Test
	public void consultaRegistrosAbiertos_Ok() throws BitacoraException {

		final String placa01 = "AVF-2121";
		final String placa02 = "AVF-2122";
		final String placa03 = "AVF-2123";
		final String placa04 = "AVF-2124";

		final String fechaIngreso01 = "07-11-2024";
		final String horaIngreso01_01 = "09:12:30";
		final String horaIngreso01_02 = "09:13:30";
		final String horaIngreso01_03 = "09:14:30";
		final String horaIngreso01_04 = "09:15:30";

		// Ingresando vehículos el día 1
		bitacora.registrarIngreso(placa01, fechaIngreso01, horaIngreso01_01);
		bitacora.registrarIngreso(placa02, fechaIngreso01, horaIngreso01_02);
		bitacora.registrarIngreso(placa03, fechaIngreso01, horaIngreso01_03);
		bitacora.registrarIngreso(placa04, fechaIngreso01, horaIngreso01_04);

		// FORMATTER
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ss");

		LocalDateTime fechaHoraIngreso01_01 = LocalDateTime.parse(fechaIngreso01 + "T" + horaIngreso01_01, formatter);
		LocalDateTime fechaHoraIngreso01_02 = LocalDateTime.parse(fechaIngreso01 + "T" + horaIngreso01_02, formatter);
		LocalDateTime fechaHoraIngreso01_03 = LocalDateTime.parse(fechaIngreso01 + "T" + horaIngreso01_03, formatter);
		LocalDateTime fechaHoraIngreso01_04 = LocalDateTime.parse(fechaIngreso01 + "T" + horaIngreso01_04, formatter);

		assertEquals(bitacora.obtenerRegistrosAbiertos(), List.of(
				new RegistroEntradaSalida(placa01, fechaHoraIngreso01_01),
				new RegistroEntradaSalida(placa02, fechaHoraIngreso01_02),
				new RegistroEntradaSalida(placa03, fechaHoraIngreso01_03),
				new RegistroEntradaSalida(placa04, fechaHoraIngreso01_04)));
	}

	@Test
	public void consultaRegistrosAbiertosVacio_Ok() throws BitacoraException {

		assertThat(bitacora.obtenerRegistrosAbiertos()).isEmpty();
	}

	@Test
	public void consultarRegistrosEntreDosFechas_Ok() throws BitacoraException {

		final String placa01 = "AVF-2121";

		final String fechaIngreso01 = "07-11-2024";
		final String fechaIngreso02 = "08-11-2024";
		final String fechaIngreso03 = "09-11-2024";
		final String fechaIngreso04 = "11-11-2024";

		final String horaIngreso01_01 = "09:12:30";
		final String horaIngreso01_02 = "09:13:30";
		final String horaIngreso01_03 = "09:14:30";
		final String horaIngreso01_04 = "09:15:30";

		final String fechaSalida01 = "07-11-2024";
		final String horaSalida01 = "10:13:30";

		final String fechaSalida02 = "08-11-2024";
		final String horaSalida02 = "11:30:30";

		final String fechaSalida03 = "09-11-2024";
		final String horaSalida03 = "12:30:30";

		final String fechaSalida04 = "11-11-2024";
		final String horaSalida04 = "13:30:30";

		// Ingresando vehículos el día 1
		bitacora.registrarIngreso(placa01, fechaIngreso01, horaIngreso01_01);
		bitacora.registrarSalida(placa01, fechaSalida01, horaSalida01);

		bitacora.registrarIngreso(placa01, fechaIngreso02, horaIngreso01_02);
		bitacora.registrarSalida(placa01, fechaSalida02, horaSalida02);

		bitacora.registrarIngreso(placa01, fechaIngreso03, horaIngreso01_03);
		bitacora.registrarSalida(placa01, fechaSalida03, horaSalida03);

		bitacora.registrarIngreso(placa01, fechaIngreso04, horaIngreso01_04);
		bitacora.registrarSalida(placa01, fechaSalida04, horaSalida04);

		// FORMATTER
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ss");

		LocalDateTime fechaHoraIngreso01_01 = LocalDateTime.parse(fechaIngreso01 + "T" + horaIngreso01_01, formatter);
		LocalDateTime fechaHoraIngreso01_02 = LocalDateTime.parse(fechaIngreso02 + "T" + horaIngreso01_02, formatter);
		LocalDateTime fechaHoraIngreso01_03 = LocalDateTime.parse(fechaIngreso03 + "T" + horaIngreso01_03, formatter);
		LocalDateTime fechaHoraIngreso01_04 = LocalDateTime.parse(fechaIngreso04 + "T" + horaIngreso01_04, formatter);

		LocalDateTime fechaHoraSalida01 = LocalDateTime.parse(fechaSalida01 + "T" + horaSalida01, formatter);
		LocalDateTime fechaHoraSalida02 = LocalDateTime.parse(fechaSalida02 + "T" + horaSalida02, formatter);
		LocalDateTime fechaHoraSalida03 = LocalDateTime.parse(fechaSalida03 + "T" + horaSalida03, formatter);
		LocalDateTime fechaHoraSalida04 = LocalDateTime.parse(fechaSalida04 + "T" + horaSalida04, formatter);

		String fechaInicio = "07-11-2024";
		String fechaFin = "12-11-2024";

		RegistroEntradaSalida registro01 = new RegistroEntradaSalida(placa01, fechaHoraIngreso01_01);
		registro01.setSalida(fechaHoraSalida01);

		RegistroEntradaSalida registro02 = new RegistroEntradaSalida(placa01, fechaHoraIngreso01_02);
		registro02.setSalida(fechaHoraSalida02);

		RegistroEntradaSalida registro03 = new RegistroEntradaSalida(placa01, fechaHoraIngreso01_03);
		registro03.setSalida(fechaHoraSalida03);

		RegistroEntradaSalida registro04 = new RegistroEntradaSalida(placa01, fechaHoraIngreso01_04);
		registro04.setSalida(fechaHoraSalida04);

		assertEquals(bitacora.obtenerRegistrosEntreFechas(fechaInicio, fechaFin, placa01), List.of(
				registro01,
				registro02,
				registro03,
				registro04));

	}

	@Test
	public void consultaConrRangoMayorATreintaDias_Fail() throws BitacoraException {
		final String placa01 = "AVF-2121";

		final String fechaIngreso01 = "07-11-2024";
		final String fechaIngreso02 = "08-11-2024";
		final String fechaIngreso03 = "09-11-2024";
		final String fechaIngreso04 = "11-11-2024";

		final String horaIngreso01_01 = "09:12:30";
		final String horaIngreso01_02 = "09:13:30";
		final String horaIngreso01_03 = "09:14:30";
		final String horaIngreso01_04 = "09:15:30";

		final String fechaSalida01 = "07-11-2024";
		final String horaSalida01 = "10:13:30";

		final String fechaSalida02 = "08-11-2024";
		final String horaSalida02 = "11:30:30";

		final String fechaSalida03 = "09-11-2024";
		final String horaSalida03 = "12:30:30";

		final String fechaSalida04 = "11-11-2024";
		final String horaSalida04 = "13:30:30";

		// Ingresando vehículos el día 1
		bitacora.registrarIngreso(placa01, fechaIngreso01, horaIngreso01_01);
		bitacora.registrarSalida(placa01, fechaSalida01, horaSalida01);

		bitacora.registrarIngreso(placa01, fechaIngreso02, horaIngreso01_02);
		bitacora.registrarSalida(placa01, fechaSalida02, horaSalida02);

		bitacora.registrarIngreso(placa01, fechaIngreso03, horaIngreso01_03);
		bitacora.registrarSalida(placa01, fechaSalida03, horaSalida03);

		bitacora.registrarIngreso(placa01, fechaIngreso04, horaIngreso01_04);
		bitacora.registrarSalida(placa01, fechaSalida04, horaSalida04);

		String fechaInicio = "07-11-2024";
		String fechaFin = "12-12-2025";

		assertThatExceptionOfType(BitacoraException.class).isThrownBy(() -> {
				bitacora.obtenerRegistrosEntreFechas(fechaInicio, fechaFin, placa01);
        }).withMessage(Bitacora.MSG_RANGO_FECHAS_MAYOR_A_30_DIAS);
	}

	@Test
	public void consultaConrRangoFechaInicioMayorAFechaActual_Fail() throws BitacoraException {
		final String placa01 = "AVF-2121";

		final String fechaIngreso01 = "07-11-2024";
		final String fechaIngreso02 = "08-11-2024";
		final String fechaIngreso03 = "09-11-2024";
		final String fechaIngreso04 = "11-11-2024";

		final String horaIngreso01_01 = "09:12:30";
		final String horaIngreso01_02 = "09:13:30";
		final String horaIngreso01_03 = "09:14:30";
		final String horaIngreso01_04 = "09:15:30";

		final String fechaSalida01 = "07-11-2024";
		final String horaSalida01 = "10:13:30";

		final String fechaSalida02 = "08-11-2024";
		final String horaSalida02 = "11:30:30";

		final String fechaSalida03 = "09-11-2024";
		final String horaSalida03 = "12:30:30";

		final String fechaSalida04 = "11-11-2024";
		final String horaSalida04 = "13:30:30";

		bitacora.registrarIngreso(placa01, fechaIngreso01, horaIngreso01_01);
		bitacora.registrarSalida(placa01, fechaSalida01, horaSalida01);

		bitacora.registrarIngreso(placa01, fechaIngreso02, horaIngreso01_02);
		bitacora.registrarSalida(placa01, fechaSalida02, horaSalida02);

		bitacora.registrarIngreso(placa01, fechaIngreso03, horaIngreso01_03);
		bitacora.registrarSalida(placa01, fechaSalida03, horaSalida03);

		bitacora.registrarIngreso(placa01, fechaIngreso04, horaIngreso01_04);
		bitacora.registrarSalida(placa01, fechaSalida04, horaSalida04);

		String fechaInicio = "07-11-2026";
		String fechaFin = "12-11-2024";

		assertThatExceptionOfType(BitacoraException.class).isThrownBy(() -> {
				bitacora.obtenerRegistrosEntreFechas(fechaInicio, fechaFin, placa01);
		}).withMessage(Bitacora.MSG_FECHA_INICIO_MAYOR_A_LA_ACTUAL);
	}

}
