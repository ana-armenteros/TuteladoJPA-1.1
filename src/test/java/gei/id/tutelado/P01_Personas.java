package gei.id.tutelado;

import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.dao.PersonaDao;
import gei.id.tutelado.dao.PersonaDaoJPA;
import gei.id.tutelado.model.Persona;

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
	
    public void test01_Recuperacion() {
    	
    	Persona p;
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		productorDatos.creaPersonas();
    	productorDatos.guardaPersonas();
    	
    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de recuperación desde la BD de persona por nif\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Recuperación por nif existente\n"
    			+ "\t\t\t\t b) Recuperacion por nif inexistente\n");

    	log.info("Probando recuperacion por nif EXISTENTE --------------------------------------------------");

    	p = personaDao.recuperaPorNif(productorDatos.p0.getNif());
    	Assert.assertEquals(productorDatos.p0.getNif(),      p.getNif());
    	Assert.assertEquals(productorDatos.p0.getNombre(),     p.getNombre());
    	Assert.assertEquals(productorDatos.p0.getApellido(), p.getApellido());
    	Assert.assertEquals(productorDatos.p0.getNacionalidad(), p.getNacionalidad());
    	Assert.assertEquals(productorDatos.p0.getTelefono(), p.getTelefono());
    	Assert.assertEquals(productorDatos.p0.getEmail(), p.getEmail());

    	log.info("");	
		log.info("Probando recuperacion por nif INEXISTENTE -----------------------------------------------");
    	
    	p = personaDao.recuperaPorNif("iwbvyhuebvuwebvi");
    	Assert.assertNull (p);

    } 	

    @Test 
    public void test02_Alta() {

    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");
  
		productorDatos.creaPersonas();
    	
    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de guardado en la BD de nueva persona\n");	
    	
    	Assert.assertNull(productorDatos.p0.getId());
    	personaDao.almacena(productorDatos.p0);    	
    	Assert.assertNotNull(productorDatos.p0.getId());
    }
    
    @Test 
    public void test03_Eliminacion() {
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		productorDatos.creaPersonas();
    	productorDatos.guardaPersonas();
 	
    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de eliminación de la BD de persona\n");   

    	Assert.assertNotNull(personaDao.recuperaPorNif(productorDatos.p0.getNif()));
    	personaDao.elimina(productorDatos.p0);    	
    	Assert.assertNull(personaDao.recuperaPorNif(productorDatos.p0.getNif()));
    } 	

    @Test 
    public void test04_Modificacion() {
    	
    	Persona p1, p2;
    	String nuevoNombre;
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		productorDatos.creaPersonas();
    	productorDatos.guardaPersonas();

    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de modificación da información básica de una persona\n");

		nuevoNombre = new String ("Nuevo Alfredo");

		p1 = personaDao.recuperaPorNif(productorDatos.p0.getNif());
		Assert.assertNotEquals(nuevoNombre, p1.getNombre());
    	p1.setNombre(nuevoNombre);

    	personaDao.modifica(p1);    	
    	
		p2 = personaDao.recuperaPorNif(productorDatos.p0.getNif());
		Assert.assertEquals (nuevoNombre, p2.getNombre());

    } 	
    
    @Test
    public void test09_Excepcions() {
    	
    	Boolean excepcion;
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		productorDatos.creaPersonas();
    	personaDao.almacena(productorDatos.p0);
    	
    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de violación de restricciones not null y unique\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Guardado de persona con nif duplicado\n"
    			+ "\t\t\t\t b) Guardado de persona con nif nulo\n");
    	
		log.info("Probando Guardado de persona con Nif duplicado -----------------------------------------------");
    	productorDatos.p1.setNif(productorDatos.p0.getNif());
    	try {
        	personaDao.almacena(productorDatos.p1);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);
    	
    	log.info("");	
		log.info("Probando Guardado de persona con Nif nulo ----------------------------------------------------");
    	productorDatos.p1.setNif(null);
    	try {
        	personaDao.almacena(productorDatos.p1);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);
    } 	
    
}