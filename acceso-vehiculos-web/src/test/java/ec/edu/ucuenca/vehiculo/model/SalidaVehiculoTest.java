package ec.edu.ucuenca.vehiculo.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests para el requerimiento de salida de vehículos. Nótese que los nombres
 * de los tests están en lenguaje natural. Cuando se dificulta dar un nombre
 * a un test, se puede usar el criterio propuesto por BDD: Given...When...Then
 */
public class SalidaVehiculoTest extends BaseTest {

	private Bitacora bitacora;
	private String placaValida;
	
	@BeforeEach
	public void init() {
		bitacora = new Bitacora();
		bitacora.registroDAO = this.registroEntradaSalidaDAO;
		
		placaValida = "AVC-123";
	}
	
	@Test
	public void salidaDeVehiculoQueHaIngresadoPreviamenteTest() throws BitacoraException {
		String fechaIngreso = "07-11-2024";
        String horaIngreso = "09:12:30";
        bitacora.registrarIngreso(placaValida, fechaIngreso, horaIngreso);

        String fechaSalida = "07-11-2024";
        String horaSalida = "09:12:30";
        
        bitacora.registrarSalida(placaValida, fechaSalida, horaSalida);
	}
	
	@Test
    public void validarSalidaDeUnVehiculoQueHaIngresadoPreviamenteTest() throws BitacoraException {

		String fechaIngreso = "07-11-2024";
        String horaIngreso = "09:12:30";
        bitacora.registrarIngreso(placaValida, fechaIngreso, horaIngreso);

        String fechaSalida = "07-11-2024";
        String horaSalida = "09:12:30";
        
        bitacora.registrarSalida(placaValida, fechaSalida, horaSalida);

        Boolean resultado = bitacora.validarVehiculoHaSalido(placaValida, fechaSalida, horaSalida);
        
        assertThat(resultado).isTrue();
    }
	
	@Test
	public void validarQueUnVehiculoQueSoloHaIngresadoNoTieneUnaSalidaRegistradaTest() throws BitacoraException {

		String fechaIngreso = "07-11-2024";
        String horaIngreso = "09:12:30";
        bitacora.registrarIngreso(placaValida, fechaIngreso, horaIngreso);
        
        // No hay salida
        String fechaSalida = "07-11-2024";
        String horaSalida = "09:12:30";
        
        Boolean resultado = bitacora.validarVehiculoHaSalido(placaValida, fechaSalida, horaSalida);
        
        assertThat(resultado).isFalse();
	}
	
	@Test
	public void salidaVehiculoFueraDeHorarioEntreSemanaTest() throws BitacoraException {
		String fechaIngreso = "07-11-2024";
        String horaIngreso = "09:12:30";
        bitacora.registrarIngreso(placaValida, fechaIngreso, horaIngreso);

        String fechaSalida = "07-11-2024";
        String horaSalida = "20:30:01";
        
        assertThatExceptionOfType(BitacoraException.class).isThrownBy(() -> {
        	bitacora.registrarSalida(placaValida, fechaSalida, horaSalida);
        }).withMessage(Bitacora.MSG_HORARIO_SALIDA_NO_ADMITIDO);
	}
	
	@Test
	public void salidaVehiculoFueraDeHorarioEnSabadoTest() throws BitacoraException {
		String fechaIngreso = "09-11-2024";
        String horaIngreso = "09:12:30";
        bitacora.registrarIngreso(placaValida, fechaIngreso, horaIngreso);

        String fechaSalida = "09-11-2024";
        String horaSalida = "13:30:01";
        
        assertThatExceptionOfType(BitacoraException.class).isThrownBy(() -> {
        	bitacora.registrarSalida(placaValida, fechaSalida, horaSalida);
        }).withMessage(Bitacora.MSG_HORARIO_SALIDA_NO_ADMITIDO);
	}
	
	@Test
	public void salidaVehiculoDebeSerElMismoDiaDeUltimoIngresoTest() throws BitacoraException {
		String fechaIngreso = "07-11-2024"; // es jueves
        String horaIngreso = "07:12:30";
        bitacora.registrarIngreso(placaValida, fechaIngreso, horaIngreso);
        
        String fechaSalida = "08-11-2024"; // es viernes
        String horaSalida = "12:30:01";
        
        assertThatExceptionOfType(BitacoraException.class).isThrownBy(() -> {
        	bitacora.registrarSalida(placaValida, fechaSalida, horaSalida);
        }).withMessage(Bitacora.MSG_LA_SALIDA_DEBE_SER_EL_MISMO_DIA_DE_ENTRADA);
	}
	
	@Test
	public void salidaVehiculoQueNoHaIngresadoTest() {
		String fechaSalida = "08-11-2024"; // es viernes
        String horaSalida = "12:30:01";
        
        assertThatExceptionOfType(BitacoraException.class).isThrownBy(() -> {
        	bitacora.registrarSalida(placaValida, fechaSalida, horaSalida);
        }).withMessage(Bitacora.MSG_VEHICULO_NO_HA_INGRESADO);
	}
}
