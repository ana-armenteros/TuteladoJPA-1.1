package gei.id.tutelado.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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


}

