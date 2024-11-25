package ec.edu.ucuenca.vehiculo.model;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

import ec.edu.ucuenca.vehiculo.dao.RegistroEntradaSalidaDAO;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

@Stateless
public class Bitacora implements Serializable {

	private static final long serialVersionUID = -3928640617105690837L;
	
	public static final String MSG_PLACA_INVALIDA = "Placa incorrecta";
	public static final String MSG_HORARIO_INGRESO_NO_ADMITIDO = "Horario de ingreso no admitido";
	public static final String MSG_VEHICULO_AUN_NO_HA_SALIDO = "El vehículo tiene un ingreso previo y aún no ha salido";
	public static final String MSG_INGRESO_EN_TIEMPO_POSTERIOR_AL_ACTUAL = "No se puede ingresar en un instante posterior al actual (tolerancia de 10 minutos)";
	public static final String MSG_HORARIO_SALIDA_NO_ADMITIDO = "Horario de salida no admitido";
	public static final String MSG_LA_SALIDA_DEBE_SER_EL_MISMO_DIA_DE_ENTRADA = "La salida debe ser el mismo día de ingreso";
	public static final String MSG_VEHICULO_NO_HA_INGRESADO = "El vehículo no registra un ingreso";
	public static final Object MSG_INGRESO_REGISTRADO_EXITOSAMENTE = "Ingreso registrado exitosamente";
	public static final Object MSG_SALIDA_REGISTRADO_EXITOSAMENTE = "Salida registrada exitosamente";
	
	@EJB
	protected RegistroEntradaSalidaDAO registroDAO;

	
	/**
	 * Registra el ingreso de un vehículo.
	 * @param placa
	 * @param fechaIngreso
	 * @param horaIngreso
	 * @throws BitacoraException cuando falla alguna validación
	 */
    public void registrarIngreso(String placa, String fechaIngreso, String horaIngreso) throws BitacoraException {
    	
    	LocalDateTime fechaHoraIngreso = obtenerFechaHora(fechaIngreso, horaIngreso);

    	try {
	    	validarPlaca(placa);
	    	validarIngresoPosteriorAlInstanteActual(fechaHoraIngreso);
	    	validarHorarioIngreso(fechaHoraIngreso);
	    	validarIngresoAbierto(placa);
    	} catch(RuntimeException e) {
    		throw new BitacoraException(e.getMessage());
    	}
    	
        RegistroEntradaSalida registroES = new RegistroEntradaSalida(placa, fechaHoraIngreso);
        registroDAO.add(registroES);
    }

    
	private void validarIngresoPosteriorAlInstanteActual(LocalDateTime fechaHoraIngreso) {
		LocalDateTime ahoraMasDiezMinutos = LocalDateTime.now().plusMinutes(10);
		
		// Si ahoraMasDiezMinutos es anterior a la fechaHoraIngreso,
		// es un error:
		if(ahoraMasDiezMinutos.isBefore(fechaHoraIngreso))
			throw new RuntimeException(MSG_INGRESO_EN_TIEMPO_POSTERIOR_AL_ACTUAL);
	}
	

	private void validarIngresoAbierto(String placa) {
		if(obtenerUltimoIngresoAbierto(placa) != null)
			throw new RuntimeException(MSG_VEHICULO_AUN_NO_HA_SALIDO);
	}
	

	private RegistroEntradaSalida obtenerUltimoIngresoAbierto(String placa) {
		RegistroEntradaSalida ultimoIngresoAbierto = null;
		final List<RegistroEntradaSalida> todosRegistros = registroDAO.getAll();
		for(RegistroEntradaSalida registroEntradaSalida : todosRegistros) {
    		if(registroEntradaSalida.getPlaca().equals(placa)
    				&& registroEntradaSalida.getSalida() == null) {
    			ultimoIngresoAbierto = registroEntradaSalida;
    			break;
    		}
    	}
		return ultimoIngresoAbierto;
	}
	

	private LocalDateTime obtenerFechaHora(String fecha, String hora) {
		return LocalDateTime.parse(fecha + " " + hora, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
	}
	

    public Boolean validarVehiculoHaIngresado(String placa, String fechaIngreso, String horaIngreso) {
        final List<RegistroEntradaSalida> todosRegistros = registroDAO.getAll();
		for(RegistroEntradaSalida registro : todosRegistros){
            if(registro.getPlaca().equals(placa)){
                return true;
            }
        }
        return false;
    }
    
    
	private void validarPlaca(String placa) {
		if(!(Pattern.compile("^\\w\\w\\w\\-\\d\\d\\d\\d$", Pattern.CASE_INSENSITIVE).matcher(placa).find()
    			|| Pattern.compile("^\\w\\w\\w\\-\\d\\d\\d$", Pattern.CASE_INSENSITIVE).matcher(placa).find())) {
			throw new RuntimeException(MSG_PLACA_INVALIDA);
		}
	}

	
	/**
	 * Registra la salida de un vehículo
	 * @param placaValida
	 * @param fechaSalida
	 * @param horaSalida
	 * @throws BitacoraException cuando falla una validación
	 */
	public void registrarSalida(String placaValida, String fechaSalida, String horaSalida) throws BitacoraException {
		
		LocalDateTime fechaHoraSalida = obtenerFechaHora(fechaSalida, horaSalida);
		
		try {
			validarDiaSalidaMismoDiaUltimoIngreso(placaValida, fechaHoraSalida);
			validarHorarioSalida(fechaHoraSalida);
		} catch(RuntimeException e) {
			throw new BitacoraException(e.getMessage());
		}
		
		RegistroEntradaSalida registroEntradaSalida = obtenerUltimoIngresoAbierto(placaValida);
		registroEntradaSalida.setSalida(fechaHoraSalida);
		registroDAO.update(registroEntradaSalida);
	}

	
	private void validarDiaSalidaMismoDiaUltimoIngreso(String placa, LocalDateTime fechaHoraSalida) {
		RegistroEntradaSalida ultimoRegistroIngresoAbierto = obtenerUltimoIngresoAbierto(placa);
		
		if(ultimoRegistroIngresoAbierto == null) {
			throw new RuntimeException(MSG_VEHICULO_NO_HA_INGRESADO);
		}
		
		if(!(ultimoRegistroIngresoAbierto.getIngreso().getYear() == fechaHoraSalida.getYear()
				&& ultimoRegistroIngresoAbierto.getIngreso().getDayOfYear() == fechaHoraSalida.getDayOfYear())) {
			throw new RuntimeException(MSG_LA_SALIDA_DEBE_SER_EL_MISMO_DIA_DE_ENTRADA);
		}
	}

	
	public Boolean validarVehiculoHaSalido(String placaValida, String fechaSalida, String horaSalida) {
		
		final List<RegistroEntradaSalida> todosRegistros = registroDAO.getAll();
		for(RegistroEntradaSalida registroEntradaSalida : todosRegistros) {
			if(registroEntradaSalida.getSalida() != null
				&& registroEntradaSalida.getPlaca().equals(placaValida)
				&& registroEntradaSalida.getSalida().equals(obtenerFechaHora(fechaSalida, horaSalida)))
					return true;
		}
		return false;
	}
	
	
	private void validarHorarioIngreso(LocalDateTime fechaHoraIngreso) {
		
		if(fechaHoraIngreso.getDayOfWeek().equals(DayOfWeek.SUNDAY))
			throw new RuntimeException(MSG_HORARIO_INGRESO_NO_ADMITIDO);

		if(fechaHoraIngreso.getDayOfWeek().equals(DayOfWeek.MONDAY)
				|| fechaHoraIngreso.getDayOfWeek().equals(DayOfWeek.TUESDAY)
				|| fechaHoraIngreso.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)
				|| fechaHoraIngreso.getDayOfWeek().equals(DayOfWeek.THURSDAY)
				|| fechaHoraIngreso.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
			if(fechaHoraIngreso.getHour() > 19  || fechaHoraIngreso.getHour() < 7)
				throw new RuntimeException(MSG_HORARIO_INGRESO_NO_ADMITIDO);
		}
		
		if(fechaHoraIngreso.getDayOfWeek().equals(DayOfWeek.SATURDAY))
			if(fechaHoraIngreso.getHour() > 12 || fechaHoraIngreso.getHour() < 8)
				throw new RuntimeException(MSG_HORARIO_INGRESO_NO_ADMITIDO);
	}
	
	
	private void validarHorarioSalida(LocalDateTime fechaHoraSalida) {
		if(fechaHoraSalida.getDayOfWeek().equals(DayOfWeek.SUNDAY))
			throw new RuntimeException(MSG_HORARIO_SALIDA_NO_ADMITIDO);

		if(fechaHoraSalida.getDayOfWeek().equals(DayOfWeek.MONDAY)
				|| fechaHoraSalida.getDayOfWeek().equals(DayOfWeek.TUESDAY)
				|| fechaHoraSalida.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)
				|| fechaHoraSalida.getDayOfWeek().equals(DayOfWeek.THURSDAY)
				|| fechaHoraSalida.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
			if((fechaHoraSalida.getHour() > 19  && fechaHoraSalida.getMinute() > 30)
					|| (fechaHoraSalida.getHour() == 20  && fechaHoraSalida.getMinute() == 30 && fechaHoraSalida.getSecond() > 0))
				throw new RuntimeException(MSG_HORARIO_SALIDA_NO_ADMITIDO);
		}
		
		if(fechaHoraSalida.getDayOfWeek().equals(DayOfWeek.SATURDAY))
			if((fechaHoraSalida.getHour() > 12 && fechaHoraSalida.getMinute() > 30)
					|| (fechaHoraSalida.getHour() == 13 && fechaHoraSalida.getMinute() == 30 && fechaHoraSalida.getSecond() > 0))
				throw new RuntimeException(MSG_HORARIO_SALIDA_NO_ADMITIDO);
	}


	public List<RegistroEntradaSalida> recuperarTodos() {
		final List<RegistroEntradaSalida> todosRegistros = registroDAO.getAll();
		return todosRegistros;
	}


	public List<RegistroEntradaSalida> recuperarRegistrosPorPlaca(String placa) {
		final List<RegistroEntradaSalida> registros = registroDAO.findByPlate(placa);
		return registros;
	}
    
}
