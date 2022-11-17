package gei.id.tutelado.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Reserva;

public class ReservaDaoJPA implements ReservaDao {

	private EntityManagerFactory emf; 
	private EntityManager em;
    
	@Override
	public void setup (Configuracion config) {
		this.emf = (EntityManagerFactory) config.get("EMF");
	}
	

	@Override
	public Reserva almacena(Reserva reserva) {
		try {
				
			em = emf.createEntityManager();
			em.getTransaction().begin();

			em.persist(reserva);

			em.getTransaction().commit();
			em.close();
		
		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return reserva;
	}

	@Override
	public Reserva modifica(Reserva reserva) {
		try {

			em = emf.createEntityManager();		
			em.getTransaction().begin();

			reserva = em.merge (reserva);

			em.getTransaction().commit();
			em.close();
			
		} catch (Exception ex ) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw(ex);
			}
		}
		return reserva;
	}

	@Override
	public void elimina(Reserva reserva) {
		try {

			em = emf.createEntityManager();
			em.getTransaction().begin();

			Reserva reservaTmp = em.find (Reserva.class, reserva.getId());
			em.remove (reservaTmp);

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
	public Reserva recuperaPorCodigo(String codigo) {
		List <Reserva> reservas=null;

		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			reservas = em.createNamedQuery("Reserva.recuperaPorCodigo", Reserva.class).setParameter("codigo", codigo).getResultList(); 

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

		return (reservas.size()!=0?reservas.get(0):null);
	}

	@Override
	public List<Reserva> obtenerReservasGrupales() {
		List <Reserva> reservas=null;

		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			reservas = em.createNamedQuery("Reserva.obtenerReservasGrupales", Reserva.class).getResultList(); 

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

		return reservas;
	}


}

