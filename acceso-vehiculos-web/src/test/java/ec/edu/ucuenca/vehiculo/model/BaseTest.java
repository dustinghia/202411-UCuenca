package ec.edu.ucuenca.vehiculo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;

import ec.edu.ucuenca.vehiculo.dao.RegistroEntradaSalidaDAO;
import ec.edu.ucuenca.vehiculo.util.JpaEntityManagerProviderExtensionForJUnit;

public class BaseTest {

	@RegisterExtension
	JpaEntityManagerProviderExtensionForJUnit entityManagerProvider = new JpaEntityManagerProviderExtensionForJUnit("pu-bdd-limpia-test");
	
	protected RegistroEntradaSalidaDAO registroEntradaSalidaDAO = new RegistroEntradaSalidaDAO();
	
	@BeforeEach
	public void setup() {
		registroEntradaSalidaDAO.setEm(entityManagerProvider.getEntityManager());
	}
	
}
