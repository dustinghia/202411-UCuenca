package ec.edu.ucuenca.vehiculo.model;

import java.io.Serializable;

public class RegistroDTO implements Serializable {

	private static final long serialVersionUID = -6384994578601545890L;

	private String placa;
	private String fecha;
	private String hora;

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	@Override
	public String toString() {
		return "RegistroDTO [placa=" + placa + ", fecha=" + fecha + ", hora=" + hora + "]";
	}
}
