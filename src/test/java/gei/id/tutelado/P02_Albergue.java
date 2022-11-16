package gei.id.tutelado;

import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.dao.AlbergueDao;
import gei.id.tutelado.dao.AlbergueDaoJPA;
import gei.id.tutelado.dao.ReservaDao;
import gei.id.tutelado.dao.ReservaDaoJPA;
import gei.id.tutelado.model.Albergue;
import gei.id.tutelado.model.Reserva;

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
import org.hibernate.LazyInitializationException;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class P02_Albergue {

    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static ProductorDatosPrueba productorDatos = new ProductorDatosPrueba();
    
    private static Configuracion cfg;
    private static AlbergueDao albergueDao;
	private static ReservaDao reservaDao;
    
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

    	albergueDao = new AlbergueDaoJPA();
    	albergueDao.setup(cfg);

		reservaDao = new ReservaDaoJPA();
		reservaDao.setup(cfg);
    	
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
    	
    	Albergue a0prueba;
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		productorDatos.crearAlberguesSueltos();
    	productorDatos.grabarAlbergues();
    	
    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de recuperación desde la BD da0e Albergue por CRU\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Recuperación por CRU existente de Albergue\n"
    			+ "\t\t\t\t b) Recuperacion por CRU inexistente de Albergue\n");

    	log.info("Probando recuperacion por CRU EXISTENTE de Albergue --------------------------------------------------");

		a0prueba = albergueDao.recuperaPorCru(productorDatos.a0.getCru());

    	Assert.assertEquals(productorDatos.a0.getCru(), a0prueba.getCru());
    	Assert.assertEquals(productorDatos.a0.getNombre(), a0prueba.getNombre());
		Assert.assertEquals(productorDatos.a0.getPoblacion(), a0prueba.getPoblacion());
    	Assert.assertEquals(productorDatos.a0.getCamino(), a0prueba.getCamino());
    	Assert.assertEquals(productorDatos.a0.getEtapa(), a0prueba.getEtapa());
    	Assert.assertEquals(productorDatos.a0.getDisponible(), a0prueba.getDisponible());
    	/*
		 * Como la coleccion de servicios, tiene la asociacion definiada con estrategia tipo LAZY
		 * no podemos recuperar los objetos como tal, por lo que al intenta recuperarlo saltaria 
		 * una excepcion tipo LazyInitializationException
		 */
		//Assert.assertEquals(productorDatos.a0.getServicios(), a0prueba.getServicios());

    	log.info("");	
		log.info("Probando recuperacion por CRU INEXISTENTE de Albergue -----------------------------------------------");
    	
		a0prueba = albergueDao.recuperaPorCru("bkababjkcbdh");
    	Assert.assertNull(a0prueba);

    } 	
 
    @Test 
    public void test02_Alta() {

    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");
		
		productorDatos.crearAlberguesSueltos();
		//Los objetos estan en estado transitorio

    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de guardado en la BD de un nuevo Albergue\n");	
    	
		Assert.assertNull(productorDatos.a0.getId());
    	albergueDao.almacena(productorDatos.a0);    	
    	Assert.assertNotNull(productorDatos.a0.getId()); 

    	Assert.assertNull(productorDatos.a1.getId());
    	albergueDao.almacena(productorDatos.a1);    	
    	Assert.assertNotNull(productorDatos.a1.getId());

    }
    
    @Test 
    public void test03_Eliminacion() {
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		productorDatos.crearAlberguesSueltos();
    	productorDatos.grabarAlbergues();
 	
    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de eliminación de la BD de Albergue\n");   

    	Assert.assertNotNull(albergueDao.recuperaPorCru(productorDatos.a0.getCru()));
    	albergueDao.elimina(productorDatos.a0);    	
    	Assert.assertNull(albergueDao.recuperaPorCru(productorDatos.a0.getCru()));

    } 	

    @Test 
    public void test04_Modificacion() {
    	
    	Albergue a0prueba, a1prueba;
    	String nuevoNombreAlbergue; 
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		productorDatos.crearAlberguesSueltos();
    	productorDatos.grabarAlbergues();

    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de modificación da información básica de un Albergue\n");

		nuevoNombreAlbergue = new String ("Nuevo Albergue San Lorenzo");

		a0prueba = albergueDao.recuperaPorCru(productorDatos.a0.getCru());
		Assert.assertNotEquals(nuevoNombreAlbergue, a0prueba.getNombre());
    	a0prueba.setNombre(nuevoNombreAlbergue);

    	albergueDao.modifica(a0prueba);    	
    	
		a1prueba = albergueDao.recuperaPorCru(productorDatos.a0.getCru());
		Assert.assertEquals (nuevoNombreAlbergue, a1prueba.getNombre());

    } 	
    
	@Test 
    public void test05_Propagacion_REMOVE() {
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");
   	
		productorDatos.crearAlbergueconReservas();
		productorDatos.grabarAlbergues();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Prueba de eliminación de un Albergue con Reservas asociadas\n");

    	// Situación de partida: a0, r0, r2 desligados

    	Assert.assertNotNull(albergueDao.recuperaPorCru(productorDatos.a0.getCru()));
		Assert.assertNotNull(reservaDao.recuperaPorCodigo(productorDatos.r2.getCodigo()));
		Assert.assertNotNull(reservaDao.recuperaPorCodigo(productorDatos.r2.getCodigo()));
		
		// Aqui o remove sobre a0 debe propagarse a las reservas r0 y r2 
		albergueDao.elimina(productorDatos.a0); 

		Assert.assertNull(albergueDao.recuperaPorCru(productorDatos.a0.getCru()));
		Assert.assertNull(reservaDao.recuperaPorCodigo(productorDatos.r0.getCodigo()));
		Assert.assertNull(reservaDao.recuperaPorCodigo(productorDatos.r2.getCodigo()));
		
    } 

	@Test 
    public void test07_EAGER_Albergue() {
    	
    	Reserva r1prueba;
    	Boolean excepcion;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		productorDatos.crearAlbergueconReservas();
		productorDatos.grabarAlbergues();

		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da recuperación de propiedades EAGER\n");   

    	// Situación de partida: a1, r1 desligados
    	
		log.info("Probando (que no hay excepcion LAZY) tras acceso inicial a la propiedad EAGER fuera de sesion ----------------------------------------");
    	
    	r1prueba = reservaDao.recuperaPorCodigo(productorDatos.r1.getCodigo());  
		log.info("Acceso a Albergue con una Reserva");
    	try	{
			//Albergue a1, es igual al Albergue asociado que tiene la Reserva r1
        	Assert.assertEquals(productorDatos.a1, r1prueba.getAlbergue());
        	excepcion=false;
    	} catch (LazyInitializationException ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	};    	
    	Assert.assertFalse(excepcion);    
    }
	
	@Test 
    public void test08_LAZY_Reservas() {
    	
		Albergue a0prueba;
    	Reserva r0prueba;
    	Boolean excepcion;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		productorDatos.crearAlbergueconReservas();
		productorDatos.grabarAlbergues();

		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Prueba de la recuperación de propiedades LAZY\n" 
		+ "\t\t\t\t Casos contemplados:\n"
		+ "\t\t\t\t a) Recuperación de Albergue con colección (LAZY) de Reservas \n"
		+ "\t\t\t\t b) Carga forzada de colección LAZY da dicha coleccion\n"     	
		+ "\t\t\t\t c) Recuperacion de una Reserva suelta con referencia (EAGER) a Albergue\n");     	

    	// Situación de partida: a0, r0, r2 desligados
    	
		log.info("a) Probando (excepcion LAZY) tras acceso inicial a la propiedad LAZY  ----------------------------------------");
    	
    	a0prueba = albergueDao.recuperaPorCru(productorDatos.a0.getCru());  
		log.info("Acceso a las Reservas de un Albergue");
    	try	{
			//Comprobar Albergue a0 esta asociado a dos Reservas (r0, r2)
			Assert.assertEquals(2, a0prueba.getReservas().size());
        	Assert.assertEquals(productorDatos.r0, a0prueba.getReservas().first());
        	Assert.assertEquals(productorDatos.r2, a0prueba.getReservas().last());
        	excepcion=false;
    	} catch (LazyInitializationException ex ) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	};    	
    	Assert.assertTrue(excepcion);    

    	log.info("");
    	log.info("b) Probando carga forzada de coleccion LAZY ------------------------------------------------------------------------");
    	
		//Albergue con el proxy sin inicializar
		a0prueba = albergueDao.recuperaPorCru(productorDatos.a0.getCru());
		//Albergue con el proxy ya inicializado (forzado)
		a0prueba = albergueDao.restauraReservas(a0prueba);

		Assert.assertEquals(2, a0prueba.getReservas().size());
		Assert.assertEquals(productorDatos.r0, a0prueba.getReservas().first());
		Assert.assertEquals(productorDatos.r2, a0prueba.getReservas().last());
    	
		log.info("");
    	log.info("c) Probando acceso a la referencia EAGER ------------------------------------------------------------------------------");
		
		r0prueba = reservaDao.recuperaPorCodigo(productorDatos.r0.getCodigo());
		Assert.assertEquals(productorDatos.a0, r0prueba.getAlbergue());

    }

    @Test
    public void test09_Excepcions() {
    	
    	Boolean excepcion;
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		productorDatos.crearAlberguesSueltos();
    	albergueDao.almacena(productorDatos.a0);
    	
    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de violación de restricciones not null y unique\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Guardado de Albergue con CRU duplicado\n"
    			+ "\t\t\t\t b) Guardado de Albergue con CRU nulo\n");
    	
		log.info("Probando Guardado de Albergue con CRU duplicado -----------------------------------------------");
    	productorDatos.a1.setCru(productorDatos.a0.getCru());
    	try {
        	albergueDao.almacena(productorDatos.a1);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);
    	
    	log.info("");	
		log.info("Probando Guardado de Albergue con CRU nulo ----------------------------------------------------");
    	productorDatos.a1.setCru(null);
    	try {
        	albergueDao.almacena(productorDatos.a1);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);
    } 	
}