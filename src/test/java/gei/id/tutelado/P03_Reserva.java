package gei.id.tutelado;

import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.dao.AlbergueDao;
import gei.id.tutelado.dao.AlbergueDaoJPA;
import gei.id.tutelado.dao.ReservaDao;
import gei.id.tutelado.dao.ReservaDaoJPA;
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

import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class P03_Reserva{

    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static ProductorDatosPrueba productorDatos = new ProductorDatosPrueba();
    
    private static Configuracion cfg;
    private static ReservaDao reservaDao;
	private static AlbergueDao albergueDao;
    
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

    	reservaDao = new ReservaDaoJPA();
    	reservaDao.setup(cfg);

		albergueDao = new AlbergueDaoJPA();
		albergueDao.setup(cfg);
    	
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
    	
    	Reserva r0prueba;
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		productorDatos.crearAlbergueconReservas();
		productorDatos.grabarAlbergues();
    	
    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de recuperación desde la BD de Reservas sueltas por Codigo\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Recuperación por Codigo existente de Reserva\n"
    			+ "\t\t\t\t b) Recuperacion por Codigo inexistente de Reserva\n");

    	log.info("Probando recuperacion por Codigo EXISTENTE de Reserva --------------------------------------------------");

		r0prueba = reservaDao.recuperaPorCodigo(productorDatos.r0.getCodigo());

		Assert.assertEquals(productorDatos.r0.getCodigo(), r0prueba.getCodigo());
		Assert.assertEquals(productorDatos.r0.getFechaEntrada(), r0prueba.getFechaEntrada());
		Assert.assertEquals(productorDatos.r0.getFechaSalida(), r0prueba.getFechaSalida());

    	log.info("");	
		log.info("Probando recuperacion por Codigo INEXISTENTE de Reserva -----------------------------------------------");
    	
		r0prueba = reservaDao.recuperaPorCodigo("fmnkhjhfgfkd");
		Assert.assertNull(r0prueba);

    } 	
 
    @Test 
    public void test02_Alta() {

    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");
		
		/*	NOTA: Para poder probar el alta de una Reserva esta tiene que estar asignada con 
			un Albergue ya que tal y como estan definidas la tablas, la tabla de Reserva tiene 
			una clave foranea 'fk_albergue_reserva' que apunta a Albergue y que no puede ser NULL
		*/
		productorDatos.crearAlberguesSueltos();
		productorDatos.grabarAlbergues();
		productorDatos.crearReservasSueltas();

    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
		log.info("Obxectivo: Proba da grabación de Reservas sueltas\n"   
				+ "\t\t\t\t Casos contemplados:\n"
				+ "\t\t\t\t a) Primera Reserva vinculada a un Albergue\n"
				+ "\t\t\t\t b) Nueva Reserva en un Albergue con Reservas previas\n");     	

		//Nos aseguramos que ambos extremos estén actualizados
		productorDatos.a0.anadirReserva(productorDatos.r0);
				
		log.info("");	
		log.info("Grabando primera Reserva en un Albergue --------------------------------------------------------------------");
    	Assert.assertNull(productorDatos.r0.getId());
    	reservaDao.almacena(productorDatos.r0);
    	Assert.assertNotNull(productorDatos.r0.getId());

		productorDatos.a0.anadirReserva(productorDatos.r2);

		log.info("");
		log.info("Grabando segunda Reserva en un mismo Albergue que ya tiene Reservas previas --------------------------------------------------------------------");
    	Assert.assertNull(productorDatos.r2.getId());
    	reservaDao.almacena(productorDatos.r2);
    	Assert.assertNotNull(productorDatos.r2.getId());

    }
     
    @Test 
    public void test03_Eliminacion() {
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		productorDatos.crearAlbergueconReservas();
    	productorDatos.grabarAlbergues();
 	
    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de eliminación en la BD de Reserva suelta (asignada a Albergue)\n");   


    	Assert.assertNotNull(reservaDao.recuperaPorCodigo(productorDatos.r0.getCodigo()));
    	reservaDao.elimina(productorDatos.r0);    	
    	Assert.assertNull(reservaDao.recuperaPorCodigo(productorDatos.r0.getCodigo()));

    } 	

    @Test 
    public void test04_Modificacion() {
    	
    	Reserva r0prueba, r1prueba;
    	LocalDate nuevaFechaSalida; 
    	
    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		productorDatos.crearAlbergueconReservas();;
    	productorDatos.grabarAlbergues();

    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de modificación da información básica de una Reserva suelta\n");

		nuevaFechaSalida = LocalDate.of(2022, 6, 2);

		r0prueba = reservaDao.recuperaPorCodigo(productorDatos.r0.getCodigo());
		Assert.assertNotEquals(nuevaFechaSalida, r0prueba.getFechaSalida());
    	r0prueba.setFechaSalida(nuevaFechaSalida);

    	reservaDao.modifica(r0prueba);    	
    	
		r1prueba = reservaDao.recuperaPorCodigo(productorDatos.r0.getCodigo());
		Assert.assertEquals(nuevaFechaSalida, r1prueba.getFechaSalida());

    } 	

	@Test 
    public void test05_Propagacion_PERSIST() {
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");
		
		productorDatos.crearAlberguesSueltos();
		productorDatos.grabarAlbergues();
		productorDatos.crearReservasSueltas();
		productorDatos.crearPeregrinosSueltos();
		
		/* NOTA: Recordar quee hay que hacer persistente de forma manual tambien el Albergue al que 
		* esta asociado la reserva, ya que la referencia no puede ser NULL ambos lados deben estar actualizados
	   	*/
	   productorDatos.a0.anadirReserva(productorDatos.r0);
	   productorDatos.r0.setPeregrinos(productorDatos.listaPeregrinos);

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Prueba de grabacion de una nueva Reserva con Peregrinos (nuevos) asociados\n");

    	// Situación de partida: r0, p0, p1 transitorios

    	Assert.assertNull(productorDatos.r0.getId());
		Assert.assertNull(productorDatos.p0.getId());
		Assert.assertNull(productorDatos.p1.getId());
		
		log.info("Grabando en BD Reserva con Peregrinos ----------------------------------------------------------------------");

		// Aqui el persist sobre r0 debe propagarse a p0 y p1 
		reservaDao.almacena(productorDatos.r0);

    	Assert.assertNotNull(productorDatos.r0.getId());
		Assert.assertNotNull(productorDatos.p0.getId());
		Assert.assertNotNull(productorDatos.p1.getId());

    } 

    /* 
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
	*/
}