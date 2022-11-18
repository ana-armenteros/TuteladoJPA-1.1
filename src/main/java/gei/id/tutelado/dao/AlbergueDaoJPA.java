package gei.id.tutelado.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.LazyInitializationException;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Albergue;

public class AlbergueDaoJPA implements AlbergueDao {

	private EntityManagerFactory emf; 
	private EntityManager em;
    
	@Override
	public void setup (Configuracion config) {
		this.emf = (EntityManagerFactory) config.get("EMF");
	}
	

	@Override
	public Albergue almacena(Albergue albergue) {
		try {
				
			em = emf.createEntityManager();
			em.getTransaction().begin();

			em.persist(albergue);

			em.getTransaction().commit();
			em.close();
		
		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return albergue;
	}

	@Override
	public Albergue modifica(Albergue albergue) {
		try {

			em = emf.createEntityManager();		
			em.getTransaction().begin();

			albergue = em.merge (albergue);

			em.getTransaction().commit();
			em.close();
			
		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return albergue;
	}

	@Override
	public void elimina(Albergue albergue) {
		try {

			em = emf.createEntityManager();
			em.getTransaction().begin();

			Albergue albergueTmp = em.find (Albergue.class, albergue.getId());
			em.remove (albergueTmp);

			em.getTransaction().commit();
			em.close();
			
		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
	}

	@Override
	public Albergue recuperaPorCru(String cru) {
		List <Albergue> albergues=null;

		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			albergues = em.createNamedQuery("Albergue.recuperaPorCru", Albergue.class).setParameter("cru", cru).getResultList(); 

			em.getTransaction().commit();
			em.close();	

		}
		catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}

		return (albergues.size()!=0?albergues.get(0):null);
	}

	@Override
	public Long obtenerAlbergueDisponiblePorCamino(String camino) {

		Long albergues= new Long(0);

		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			albergues = em.createNamedQuery("Albergue.obtenerAlbergueDisponiblePorCamino", Long.class)
						.setParameter("camino", camino)
						.getSingleResult().longValue(); 

			em.getTransaction().commit();
			em.close();	

		}
		catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}

		return albergues;
	}

	@Override
	public List<Albergue> obtenerAlberguesSinReservas() {
		List <Albergue> albergues=null;

		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			albergues = em.createNamedQuery("Albergue.obtenerAlberguesSinReservas", Albergue.class).getResultList(); 

			em.getTransaction().commit();
			em.close();	

		}
		catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}

		return albergues;
	}	

	@Override
	public Albergue restauraReservas(Albergue albergue) {
		
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			try {
				albergue.getReservas().size();
			} catch (Exception ex1) {
				if (ex1 instanceof LazyInitializationException) {

					/* Vuelve a ligar el objeto Albergue a un nuevo Contexto de Persistencia
					 * y accede a la propiedad en ese momento para cargarla
					*/
					albergue = em.merge(albergue);
					albergue.getReservas().size();
				} else {
					throw ex1;
				}
			}
			em.getTransaction().commit();
			em.close();
		} catch (Exception ex) {
			if (em != null && em.isOpen()) {
				if (em.getTransaction().isActive())
					em.getTransaction().rollback();
				em.close();
				throw ex;
			}
		}

		return albergue;

	}

}

