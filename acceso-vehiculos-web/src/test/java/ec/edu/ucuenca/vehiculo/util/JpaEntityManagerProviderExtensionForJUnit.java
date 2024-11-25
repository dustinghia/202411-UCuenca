package ec.edu.ucuenca.vehiculo.util;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class JpaEntityManagerProviderExtensionForJUnit implements BeforeEachCallback, AfterEachCallback {
	
	public static final String DEFAULT_PERSISTENCE_UNIT_NAME = "persistence-unit-test";
	
	private EntityManager em;
    private EntityTransaction tx;
	private EntityManagerFactory emf;
    
    public JpaEntityManagerProviderExtensionForJUnit(String persistenceUnit) {
        emf = Persistence.createEntityManagerFactory(persistenceUnit);
        this.em = emf.createEntityManager();
        this.tx = em.getTransaction();
    }
    
    public JpaEntityManagerProviderExtensionForJUnit() {
		this(DEFAULT_PERSISTENCE_UNIT_NAME);
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		this.tx.begin();
	}
	
	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		this.tx.commit();
		if(this.em.isOpen()) {
			em.close();
		}
	}
	
	public EntityManager getEntityManager() {
		return em;
	}

	public EntityTransaction getEntityTransaction() {
		return tx;
	}
}
