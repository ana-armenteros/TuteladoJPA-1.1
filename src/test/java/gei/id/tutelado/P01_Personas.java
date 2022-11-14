package gei.id.tutelado;

import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.dao.PersonaDao;
import gei.id.tutelado.dao.PersonaDaoJPA;
import gei.id.tutelado.model.Empleado;
import gei.id.tutelado.model.Peregrino;

//import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.runners.MethodSorters;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class P01_Personas {

    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static ProductorDatosPrueba productorDatos = new ProductorDatosPrueba();
    
    private static Configuracion cfg;
    private static PersonaDao personaDao;
    
    @Rule
    public TestRule watcher = new TestWatcher() {
       protected void starting(Description description) {
    	   log.info("");
    	   log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    	   log.info("Iniciando test: " + description.getMethodName());
    	   log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
       }
       protected void finished(Description description) {
    	   log.info("");
    	   log.info("-----------------------------------------------------------------------------------------------------------------------------------------");
    	   log.info("Finalizado test: " + description.getMethodName());
    	   log.info("-----------------------------------------------------------------------------------------------------------------------------------------");
       }
    };
    
    @BeforeClass
    public static void init() throws Exception {
    	cfg = new ConfiguracionJPA();
    	cfg.start();

    	personaDao = new PersonaDaoJPA();
    	personaDao.setup(cfg);
    	
    	productorDatos = new ProductorDatosPrueba();
    	productorDatos.Setup(cfg);
    }
    
    @AfterClass
    public static void endclose() throws Exception {
    	cfg.endUp();    	
    }
    
	@Before
	public void setUp() throws Exception {		
		log.info("");	
		log.info("Limpiando BD --------------------------------------------------------------------------------------------");
		productorDatos.limpiarBD();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
    public void test01_Recuperacion() {
    	
    	Empleado e0prueba;
		Peregrino p0prueba;
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		productorDatos.crearEmpleadosSueltos();
		productorDatos.crearPeregrinosSueltos();
    	productorDatos.guardaPersonas();
    	
    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de recuperación desde la BD de persona(Empleado o Peregrino) por NIF\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Recuperación por NIF existente de Empleado y Peregrino\n"
    			+ "\t\t\t\t b) Recuperacion por NIF inexistente de Empleado y Peregrino\n");

    	log.info("Probando recuperacion por nif EXISTENTE de Empleado --------------------------------------------------");

    	e0prueba = (Empleado) personaDao.recuperaPorNif(productorDatos.e0.getNif());
    	Assert.assertEquals(productorDatos.e0.getNif(), e0prueba.getNif());
    	Assert.assertEquals(productorDatos.e0.getNombre(), e0prueba.getNombre());
    	Assert.assertEquals(productorDatos.e0.getApellido(), e0prueba.getApellido());
    	Assert.assertEquals(productorDatos.e0.getNacionalidad(), e0prueba.getNacionalidad());
    	Assert.assertEquals(productorDatos.e0.getTelefono(), e0prueba.getTelefono());
    	Assert.assertEquals(productorDatos.e0.getEmail(), e0prueba.getEmail());
		Assert.assertEquals(productorDatos.e0.getNss(), e0prueba.getNss());

		log.info("Probando recuperacion por nif EXISTENTE de Peregrino --------------------------------------------------");

		p0prueba = (Peregrino) personaDao.recuperaPorNif(productorDatos.p0.getNif());
    	Assert.assertEquals(productorDatos.p0.getNif(), p0prueba.getNif());
    	Assert.assertEquals(productorDatos.p0.getNombre(), p0prueba.getNombre());
    	Assert.assertEquals(productorDatos.p0.getApellido(), p0prueba.getApellido());
    	Assert.assertEquals(productorDatos.p0.getNacionalidad(), p0prueba.getNacionalidad());
    	Assert.assertEquals(productorDatos.p0.getTelefono(), p0prueba.getTelefono());
    	Assert.assertEquals(productorDatos.p0.getEmail(), p0prueba.getEmail());
		Assert.assertEquals(productorDatos.p0.getMedio(), p0prueba.getMedio());
		Assert.assertEquals(productorDatos.p0.getLimitacionFisica(), p0prueba.getLimitacionFisica());

    	log.info("");	
		log.info("Probando recuperacion por nif INEXISTENTE de Empleado -----------------------------------------------");
    	
    	e0prueba = (Empleado) personaDao.recuperaPorNif("iwbvyhuebvuwebvi");
    	Assert.assertNull(e0prueba);

		log.info("Probando recuperacion por nif INEXISTENTE de Peregrino -----------------------------------------------");
    	
		p0prueba = (Peregrino) personaDao.recuperaPorNif("iwbvyhuebvuwebvi");
		Assert.assertNull(p0prueba);

    } 	

    @Test 
    public void test02_Alta() {

    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");
  
		productorDatos.crearEmpleadosSueltos();
		productorDatos.crearPeregrinosSueltos();
    	
    	log.info("");	
		log.info("Inicio de la 1ª parte del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de guardado en la BD de nuevo Empleado\n");	
    	
    	Assert.assertNull(productorDatos.e0.getId());
    	personaDao.almacena(productorDatos.e0);    	
    	Assert.assertNotNull(productorDatos.e0.getId());

		log.info("");	
		log.info("Inicio de la 2ª parte del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de guardado en la BD de nuevo Peregrino\n");	
    	
    	Assert.assertNull(productorDatos.p0.getId());
    	personaDao.almacena(productorDatos.p0);    	
    	Assert.assertNotNull(productorDatos.p0.getId());
    }
    
    @Test 
    public void test03_Eliminacion() {
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		productorDatos.crearEmpleadosSueltos();
		productorDatos.crearPeregrinosSueltos();
    	productorDatos.guardaPersonas();
 	
    	log.info("");	
		log.info("Inicio de la 1ª parte del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de eliminación de la BD de Empleado\n");   

    	Assert.assertNotNull(personaDao.recuperaPorNif(productorDatos.e0.getNif()));
    	personaDao.elimina(productorDatos.e0);    	
    	Assert.assertNull(personaDao.recuperaPorNif(productorDatos.e0.getNif()));

		log.info("");	
		log.info("Inicio de la 2ª parte del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de eliminación de la BD de Peregrino\n");   

    	Assert.assertNotNull(personaDao.recuperaPorNif(productorDatos.p0.getNif()));
    	personaDao.elimina(productorDatos.p0);    	
    	Assert.assertNull(personaDao.recuperaPorNif(productorDatos.p0.getNif()));
    } 	

    @Test 
    public void test04_Modificacion() {
    	
    	Empleado e0prueba, e1prueba;
		Peregrino p0prueba, p1prueba;
    	String nuevoNombreEmpleado, nuevoNombrePeregrino;
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		productorDatos.crearEmpleadosSueltos();
		productorDatos.crearPeregrinosSueltos();
    	productorDatos.guardaPersonas();

    	log.info("");	
		log.info("Inicio la 1ª parte del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de modificación da información básica de un Empleado\n");

		nuevoNombreEmpleado = new String ("Nuevo Alfredo");

		e0prueba = (Empleado) personaDao.recuperaPorNif(productorDatos.e0.getNif());
		Assert.assertNotEquals(nuevoNombreEmpleado, e0prueba.getNombre());
    	e0prueba.setNombre(nuevoNombreEmpleado);

    	personaDao.modifica(e0prueba);    	
    	
		e1prueba = (Empleado) personaDao.recuperaPorNif(productorDatos.e0.getNif());
		Assert.assertEquals (nuevoNombreEmpleado, e1prueba.getNombre());

    	log.info("");	
		log.info("Inicio de la 2ª parte del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de modificación da información básica de un Peregrino\n");

		nuevoNombrePeregrino = new String ("Nuevo Julián");

		p0prueba = (Peregrino) personaDao.recuperaPorNif(productorDatos.p0.getNif());
		Assert.assertNotEquals(nuevoNombrePeregrino, p0prueba.getNombre());
    	p0prueba.setNombre(nuevoNombrePeregrino);

    	personaDao.modifica(p0prueba);    	
    	
		p1prueba = (Peregrino) personaDao.recuperaPorNif(productorDatos.p0.getNif());
		Assert.assertEquals(nuevoNombrePeregrino, p1prueba.getNombre());

    } 	
    
    @Test
    public void test09_Excepcions() {
    	
    	Boolean excepcion;
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		productorDatos.crearEmpleadosSueltos();
    	personaDao.almacena(productorDatos.e0);
    	
    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de violación de restricciones not null y unique\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Guardado de persona con nif duplicado\n"
    			+ "\t\t\t\t b) Guardado de persona con nif nulo\n");
    	
		log.info("Probando Guardado de persona con Nif duplicado -----------------------------------------------");
    	productorDatos.e1.setNif(productorDatos.e0.getNif());
    	try {
        	personaDao.almacena(productorDatos.e1);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);
    	
    	log.info("");	
		log.info("Probando Guardado de persona con Nif nulo ----------------------------------------------------");
    	productorDatos.e1.setNif(null);
    	try {
        	personaDao.almacena(productorDatos.e1);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);
    } 	
	
    
}