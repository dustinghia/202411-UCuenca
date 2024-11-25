package ec.edu.ucuenca.vehiculo.dao;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public abstract class GenericDAO <T extends Serializable, Pk extends Serializable>{

	private Class<T> claseEntidadPersistente;
	
	// Inyectando la unidad de persistencia para operación del sistema
	@PersistenceContext(unitName = "persistence-unit")
	protected EntityManager em;

	protected void setClass(Class<T> claseEntidadPersistente) {
		this.claseEntidadPersistente = claseEntidadPersistente;
	}
	
	public T findById(Pk id) {
		return em.find(claseEntidadPersistente, id);
	}
	
	public List<T> getAll(){
		return em.createQuery("select o from " + claseEntidadPersistente.getSimpleName() + " o", claseEntidadPersistente).getResultList();
	}
	
	public T add(T nuevaEntidad) {
		em.persist(nuevaEntidad);
		return nuevaEntidad;
	}
	
	public T update(T entidad) {
		return em.merge(entidad);
	}
	
	public void remove(T entidad) {
		em.remove(entidad);
	}
	
	public void removeById(Pk id) {
		em.remove(findById(id));
	}
	
	public void setEm(EntityManager em) {
		this.em = em;
	}
}
