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
	
	//Lista de conjunto de objetos que se van a utilizar en los casos de preuba
	public Empleado e0, e1;
	public List<Empleado> listaEmpleados;

	public Peregrino p0,p1;
	public List<Peregrino> listaPeregrinos;
	
	public void Setup (Configuracion config) {
		this.emf=(EntityManagerFactory) config.get("EMF");
	}
	
	public void crearEmpleadosSueltos() {

		// Crea a dos empleados EN MEMORIA: e0,e1

		this.e0 = new Empleado();
        this.e0.setNif("97350619Z");
        this.e0.setNombre("Alfredo");
        this.e0.setApellido("López");
        this.e0.setNacionalidad("Española");
		this.e0.setTelefono("67778");
		this.e0.setEmail("alfredo@gmail.com");
		this.e0.setNss("VB186706279101");
		this.e0.setPuesto("Recepcionista");

		this.e1 = new Empleado();
        this.e1.setNif("52542767A");
        this.e1.setNombre("Anne");
        this.e1.setApellido("Smith");
        this.e1.setNacionalidad("Inglesa");
		this.e1.setTelefono("68878");
		this.e1.setEmail("anne@gmail.com");
		this.e1.setNss("AC486933509888");
		this.e1.setPuesto("Ayudante de Cocina");

        this.listaEmpleados = new ArrayList<Empleado> ();
        this.listaEmpleados.add(0,e0);
        this.listaEmpleados.add(1,e1);        

	}

	public void crearPeregrinosSueltos() {

		// Crea a dos peregrinos EN MEMORIA: p0,p1

		this.p0 = new Peregrino();
        this.p0.setNif("84003813Q");
        this.p0.setNombre("Julián");
        this.p0.setApellido("Hernandez");
        this.p0.setNacionalidad("Española");
		this.p0.setTelefono("698125889");
		this.p0.setEmail("julianher@gmail.com");
		this.p0.setMedio("A pie");
		this.p0.setLimitacionFisica(false);

		this.p1 = new Peregrino();
        this.p1.setNif("71944947G");
        this.p1.setNombre("Valeria");
        this.p1.setApellido("Andrade");
        this.p1.setNacionalidad("Española");
		this.p1.setTelefono("881475984");
		this.p1.setEmail("valandre@gmail.com");
		this.p1.setMedio("A caballo");
		this.p1.setLimitacionFisica(true);

        this.listaPeregrinos = new ArrayList<Peregrino> ();
        this.listaPeregrinos.add(0,p0);
        this.listaPeregrinos.add(1,p1);        

	}

	
	public void guardaPersonas() {
		EntityManager em=null;
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			Iterator<Empleado> itEmp = this.listaEmpleados.iterator();
			while (itEmp.hasNext()) {
				Empleado emp = itEmp.next();
				em.persist(emp);
			}

			Iterator<Peregrino> itPer = this.listaPeregrinos.iterator();
			while (itPer.hasNext()) {
				Peregrino per = itPer.next();
				em.persist(per);
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
