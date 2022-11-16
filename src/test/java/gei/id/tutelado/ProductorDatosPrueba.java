package gei.id.tutelado;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
	public Set<Peregrino> listaPeregrinos;

	public Albergue a0, a1;
	public List<Albergue> listaAlbergues;
	public Set<String> listaServiciosa0, listaServiciosa1;

	public Reserva r0, r1, r2;
	public List<Reserva> listaReservas;
	
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
        this.listaEmpleados.add(0,this.e0);
        this.listaEmpleados.add(1,this.e1);        

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

        this.listaPeregrinos = new HashSet<Peregrino> ();
        this.listaPeregrinos.add(this.p0);
        this.listaPeregrinos.add(this.p1);        

	}

	public void crearAlberguesSueltos() {

		this.listaServiciosa0 = new HashSet<String>();
		this.listaServiciosa0.add("Maquinas expendedoras");
		this.listaServiciosa0.add("Lavadora/Secadora");
		this.listaServiciosa0.add("WiFi gratuita");

		this.listaServiciosa1 = new HashSet<String>();
		this.listaServiciosa1.add("Lavadora/Secadora");
		this.listaServiciosa1.add("WiFi gratuita");
		this.listaServiciosa1.add("Cocina compartida");

		// Crea dos albergues EN MEMORIA: a0,a1
		// Sin reservas asignadas

		this.a0 = new Albergue();
		this.a0.setCru("12345678912345");
		this.a0.setNombre("Albergue San Lorenzo de Bruma");
		this.a0.setPoblacion("Bruma");
		this.a0.setCamino("Camino Inglés");
		this.a0.setEtapa("Etapa 4: Betanzos a Hospital de Bruma");
		this.a0.setDisponible(true);
		this.a0.setServicios(this.listaServiciosa0);

		this.a1 = new Albergue();
		this.a1.setCru("98745612378945");
		this.a1.setNombre("Albergue Turístico Salceda");
		this.a1.setPoblacion("Salceda");
		this.a1.setCamino("Camino Francés");
		this.a1.setEtapa("Etapa 30: Arzúa a Pedrouzo");
		this.a1.setDisponible(true);
		this.a1.setServicios(this.listaServiciosa1);
		
        this.listaAlbergues = new ArrayList<Albergue>();
        this.listaAlbergues.add(0,this.a0);
		this.listaAlbergues.add(1,this.a1);    

	}

	public void crearReservasSueltas() {

		// Crea tres reservas EN MEMORIA: r0, r1, r2
		// Sin Albergue asignado (temporalmete), ni Peregrinos

		this.r0 = new Reserva();
		this.r0.setCodigo("R00001");
		this.r0.setFechaEntrada(LocalDate.of(2022, 6, 1));
		this.r0.setFechaSalida(LocalDate.of(2022, 6, 5));

		this.r1 = new Reserva();
		this.r1.setCodigo("R00002");
		this.r1.setFechaEntrada(LocalDate.of(2022, 6, 1));
		this.r1.setFechaSalida(LocalDate.of(2022, 6, 3));
		this.r1.setPeregrinos(this.listaPeregrinos);

		this.r2 = new Reserva();
		this.r2.setCodigo("R00003");
		this.r2.setFechaEntrada(LocalDate.of(2022, 8, 8));
		this.r2.setFechaSalida(LocalDate.of(2022, 8, 11));
		
        this.listaReservas = new ArrayList<Reserva>();
        this.listaReservas.add(0,this.r0);
		this.listaReservas.add(1,this.r1);    
		this.listaReservas.add(2,this.r2); 

	}

	public void crearAlbergueconReservas() {

		this.crearAlberguesSueltos();
		this.crearReservasSueltas();
		
		this.a0.anadirReserva(this.r0);
		this.a0.anadirReserva(this.r2);
		this.a1.anadirReserva(this.r1);

	}

	public void grabarPersonas() {
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

	public void grabarAlbergues() {
		EntityManager em=null;
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			Iterator<Albergue> itAlb = this.listaAlbergues.iterator();
			while (itAlb.hasNext()) {
				Albergue alb = itAlb.next();
				em.persist(alb);

				// DESCOMENTAR SE A PROPAGACION DO PERSIST NON ESTA ACTIVADA
				Iterator<Reserva> itRes = alb.getReservas().iterator();
				while (itRes.hasNext()) {
					em.persist(itRes.next());
				}
				
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
			Iterator<Albergue> itA = em.createNamedQuery("Albergue.recuperaTodos", Albergue.class).getResultList().iterator();
			while (itA.hasNext()) em.remove(itA.next());
			Iterator<Reserva> itR = em.createNamedQuery("Reserva.recuperaTodos", Reserva.class).getResultList().iterator();
			while (itR.hasNext()) em.remove(itR.next());

			em.createNativeQuery("UPDATE tabla_ids SET ultimo_valor_id=0 WHERE nombre_id='idPersona'" ).executeUpdate();
			em.createNativeQuery("UPDATE tabla_ids SET ultimo_valor_id=0 WHERE nombre_id='idAlbergue'" ).executeUpdate();
			em.createNativeQuery("UPDATE tabla_ids SET ultimo_valor_id=0 WHERE nombre_id='idReserva'" ).executeUpdate();

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
