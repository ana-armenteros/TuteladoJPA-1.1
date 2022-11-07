package gei.id.tutelado;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.*;

public class ProductorDatosPrueba {	
	private EntityManagerFactory emf=null;
	
	public Persona p0, p1;
	public List<Persona> listaP;
	
	public void Setup (Configuracion config) {
		this.emf=(EntityManagerFactory) config.get("EMF");
	}
	
	public void creaPersonas() {
		this.p0 = new Persona();
        this.p0.setNif("000A");
        this.p0.setNombre("Alfredo");
        this.p0.setApellido("López");
        this.p0.setNacionalidad("Española");
		this.p0.setTelefono("67778");
		this.p0.setEmail("alfredo@gmail.com");

		this.p1 = new Persona();
        this.p1.setNif("100B");
        this.p1.setNombre("Anne");
        this.p1.setApellido("Smith");
        this.p1.setNacionalidad("Inglesa");
		this.p1.setTelefono("68878");
		this.p1.setEmail("anne@gmail.com");

        this.listaP = new ArrayList<Persona> ();
        this.listaP.add(0,p0);
        this.listaP.add(1,p1);        

	}
	
	public void guardaPersonas() {
		EntityManager em=null;
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			Iterator<Persona> itP = this.listaP.iterator();
			while (itP.hasNext()) {
				Persona p = itP.next();
				em.persist(p);
				// DESCOMENTAR SE A PROPAGACION DO PERSIST NON ESTA ACTIVADA
				/*
				Iterator<EntradaLog> itEL = u.getEntradasLog().iterator();
				while (itEL.hasNext()) {
					em.persist(itEL.next());
				}
				*/
			}
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw (e);
			}
		}	
	}
	
	public void limpiarBD () {
		EntityManager em=null;
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();
			
			Iterator <Persona> itP = em.createNamedQuery("Persona.recuperaTodos", Persona.class).getResultList().iterator();
			while (itP.hasNext()) em.remove(itP.next());	
			
			em.createNativeQuery("UPDATE tabla_ids SET ultimo_valor_id=0 WHERE nombre_id='idPersona'" ).executeUpdate();

			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw (e);
			}
		}
	}
	
	
}
