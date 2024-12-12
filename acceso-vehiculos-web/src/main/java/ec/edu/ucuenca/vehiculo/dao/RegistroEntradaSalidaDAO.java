package ec.edu.ucuenca.vehiculo.dao;

import java.time.LocalDate;
import java.util.List;

import ec.edu.ucuenca.vehiculo.model.RegistroEntradaSalida;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

@Stateless
public class RegistroEntradaSalidaDAO extends GenericDAO<RegistroEntradaSalida, Long> {

	public RegistroEntradaSalidaDAO() {
		setClass(RegistroEntradaSalida.class);
	}

	public List<RegistroEntradaSalida> findByPlate(String placa) {
		String statement = "select r from RegistroEntradaSalida r where r.placa = :placa order by r.ingreso desc";
		TypedQuery<RegistroEntradaSalida> query = em.createQuery(statement, RegistroEntradaSalida.class);
		query.setParameter("placa", placa);
		return query.getResultList();
	}

	public List<RegistroEntradaSalida> getRegistrosSinSalidaRegistrada() {

		String statement = "select r from RegistroEntradaSalida r where r.salida is null";

		TypedQuery<RegistroEntradaSalida> query = em.createQuery(statement, RegistroEntradaSalida.class);

		return query.getResultList();

	}

	public List<RegistroEntradaSalida> getRegistrosEntreFechas(LocalDate fechaInicioLocalDate,
			LocalDate fechaFinLocalDate, String placa) {

		String statement = "select r from RegistroEntradaSalida r where r.ingreso between :fechaInicio and :fechaFin and r.placa = :placa";

		TypedQuery<RegistroEntradaSalida> query = em.createQuery(statement, RegistroEntradaSalida.class);

		query.setParameter("fechaInicio", fechaInicioLocalDate.atStartOfDay());

		query.setParameter("fechaFin", fechaFinLocalDate.atStartOfDay());

		query.setParameter("placa", placa);

		return query.getResultList();
	}
}
