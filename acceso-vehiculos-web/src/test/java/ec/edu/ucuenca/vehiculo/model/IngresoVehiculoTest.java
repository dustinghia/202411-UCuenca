package ec.edu.ucuenca.vehiculo.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IngresoVehiculoTest extends BaseTest {
	
	private Bitacora bitacora;
	private String placaValida;
	private String otraPlacaValida;
	private String placaInvalida;
	
	@BeforeEach
	public void init() {
		bitacora = new Bitacora();
		bitacora.registroDAO = this.registroEntradaSalidaDAO;
		
		placaValida = "AVC-123";
		otraPlacaValida = "GCC-2123";
		placaInvalida = "AVCR-1234";
	}

	@Test
    public void ingresoDeVehiculosConPlacasValidasEnHorarioValido_Ok() throws BitacoraException {
		String fechaIngreso = "07-11-2024";
        String horaIngreso = "09:12:30";
        
        bitacora.registrarIngreso(placaValida, fechaIngreso, horaIngreso);
        bitacora.registrarIngreso(otraPlacaValida, fechaIngreso, horaIngreso);
    }
	
	@Test
	public void ingresoDeVehiculoConPlacaInvalidaEnHorarioValido_DebeFallar() {
		String fechaIngreso = "07-11-2024";
        String horaIngreso = "09:12:30";
        
        assertThatExceptionOfType(BitacoraException.class).isThrownBy(()->{
        	bitacora.registrarIngreso(placaInvalida, fechaIngreso, horaIngreso);
        }).withMessage(Bitacora.MSG_PLACA_INVALIDA);
	}
	
	@Test
	public void ingresoDeVehiculoConPlacaValidaEnHorarioPosteriorAlPermitidoEntreSemana_DebeFallar() {
		String fechaIngreso = "07-11-2024"; // es jueves
        String horaIngreso = "20:01:00";
        
        assertThatExceptionOfType(BitacoraException.class).isThrownBy(()->{
        	bitacora.registrarIngreso(placaValida, fechaIngreso, horaIngreso);
        }).withMessage(Bitacora.MSG_HORARIO_INGRESO_NO_ADMITIDO);
	}
	
	@Test
	public void ingresoDeVehiculoConPlacaValidaEnHorarioAnteriorAlPermitidoEntreSemana_DebeFallar() {
		String fechaIngreso = "07-11-2024"; // es jueves
        String horaIngreso = "06:59:00";
        
        assertThatExceptionOfType(BitacoraException.class).isThrownBy(()->{
        	bitacora.registrarIngreso(placaValida, fechaIngreso, horaIngreso);
        }).withMessage(Bitacora.MSG_HORARIO_INGRESO_NO_ADMITIDO);
	}
	
	@Test
	public void ingresoDeVehiculoConPlacaValidaEnHorarioPosteriorAlPermitidoEnSabado_DebeFallar() {
		String fechaIngreso = "09-11-2024"; // es sabado
        String horaIngreso = "13:00:01";
        
        assertThatExceptionOfType(BitacoraException.class).isThrownBy(()->{
        	bitacora.registrarIngreso(placaValida, fechaIngreso, horaIngreso);
        }).withMessage(Bitacora.MSG_HORARIO_INGRESO_NO_ADMITIDO);
	}
	
	@Test
	public void ingresoDeVehiculoConPlacaValidaEnHorarioAnteriorAlPermitidoEnSabado_DebeFallar() {
		String fechaIngreso = "09-11-2024"; // es sabado
        String horaIngreso = "07:59:59";
        
        assertThatExceptionOfType(BitacoraException.class).isThrownBy(()->{
        	bitacora.registrarIngreso(placaValida, fechaIngreso, horaIngreso);
        }).withMessage(Bitacora.MSG_HORARIO_INGRESO_NO_ADMITIDO);
	}
	
	@Test
	public void ingresoDeVehiculoConPlacaValidaEnDomingoNoAdmitido_DebeFallar() {
		String fechaIngreso = "10-11-2024"; // es domingo
        String horaIngreso = "09:59:59";
        
        assertThatExceptionOfType(BitacoraException.class).isThrownBy(()->{
        	bitacora.registrarIngreso(placaValida, fechaIngreso, horaIngreso);
        }).withMessage(Bitacora.MSG_HORARIO_INGRESO_NO_ADMITIDO);
	}
	
	@Test
    public void validarVehiculoHaIngresado() throws BitacoraException {
        String fechaIngreso = "07-11-2024";
        String horaIngreso = "09:12:30";
        bitacora.registrarIngreso(placaValida, fechaIngreso, horaIngreso);

        Boolean resultado = bitacora.validarVehiculoHaIngresado(placaValida, fechaIngreso, horaIngreso);
        
        assertThat(resultado).isTrue();
    }
	
    @Test
    public void validarVehiculoNoHaIngresado() {
        String fechaIngreso = "07-11-2024";
        String horaIngreso = "09:12:30";
        // No se ha registrado ningún ingreso
        
        Boolean resultado = bitacora.validarVehiculoHaIngresado(placaValida, fechaIngreso, horaIngreso);
        
        assertThat(resultado).isFalse();
    }
    
    @Test
    public void validarVehiculoQueHaIngresadoYNoHaSalidoNoPuedeIngresarDeNuevo() throws BitacoraException {
    	String fechaPrimerIngreso = "07-11-2024";
        String horaPrimerIngreso = "09:12:30";
        bitacora.registrarIngreso(placaValida, fechaPrimerIngreso, horaPrimerIngreso);
        
        // No hay salida
        
        String fechaSegundoIngreso = "07-11-2024";
        String horaSegundoIngreso = "11:00:00";
        
        assertThatExceptionOfType(BitacoraException.class).isThrownBy(()->{
        	bitacora.registrarIngreso(placaValida, fechaSegundoIngreso, horaSegundoIngreso);
        }).withMessage(Bitacora.MSG_VEHICULO_AUN_NO_HA_SALIDO);
    }
    
    @Test
    public void validarVehiculoNoPuedeIngresarEnElFuturo() {
    	LocalDateTime ahora = LocalDateTime.now();
    	LocalDateTime fechaHoraPasaDeTiempo = ahora.plusMinutes(10).plusSeconds(1);
    	
    	String fechaIngreso = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(fechaHoraPasaDeTiempo);
        String horaIngreso = DateTimeFormatter.ofPattern("HH:mm:ss").format(fechaHoraPasaDeTiempo);
    	
        assertThatExceptionOfType(BitacoraException.class).isThrownBy(()->{
        	bitacora.registrarIngreso(placaValida, fechaIngreso, horaIngreso);
        }).withMessage(Bitacora.MSG_INGRESO_EN_TIEMPO_POSTERIOR_AL_ACTUAL);
    }
}
