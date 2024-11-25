package ec.edu.ucuenca.vehiculo.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "registro_entrada_salida")
public class RegistroEntradaSalida implements Serializable {

	private static final long serialVersionUID = 7606859129566941773L;

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "registro_id_generator")
	@SequenceGenerator(name = "registro_id_generator", sequenceName = "registro_id_seq", allocationSize = 1)
	@Id
	private Long id;
	private String placa;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime ingreso;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime salida;
	
	public RegistroEntradaSalida() {
	}

	public RegistroEntradaSalida(String placa, LocalDateTime ingreso) {
		super();
		this.placa = placa;
		this.ingreso = ingreso;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getPlaca() {
		return placa;
	}
	
	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public LocalDateTime getIngreso() {
		return ingreso;
	}

	public void setIngreso(LocalDateTime ingreso) {
		this.ingreso = ingreso;
	}

	public LocalDateTime getSalida() {
		return salida;
	}

	public void setSalida(LocalDateTime salida) {
		this.salida = salida;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ingreso, placa, salida);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegistroEntradaSalida other = (RegistroEntradaSalida) obj;
		return Objects.equals(ingreso, other.ingreso) && Objects.equals(placa, other.placa)
				&& Objects.equals(salida, other.salida);
	}

	@Override
	public String toString() {
		return "RegistroEntradaSalida [id=" + id + ", placa=" + placa + ", ingreso=" + ingreso + ", salida=" + salida
				+ "]";
	}
}
