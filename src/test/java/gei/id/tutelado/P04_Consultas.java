package gei.id.tutelado;

import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.dao.AlbergueDao;
import gei.id.tutelado.dao.AlbergueDaoJPA;
import gei.id.tutelado.dao.PersonaDao;
import gei.id.tutelado.dao.PersonaDaoJPA;
import gei.id.tutelado.dao.ReservaDao;
import gei.id.tutelado.dao.ReservaDaoJPA;
import gei.id.tutelado.model.Reserva;
import gei.id.tutelado.model.Empleado;
import gei.id.tutelado.model.Peregrino;
import gei.id.tutelado.model.Albergue;

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

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class P04_Consultas {
    
    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static ProductorDatosPrueba productorDatos = new ProductorDatosPrueba();
    
    private static Configuracion cfg;
    private static ReservaDao reservaDao;
	private static AlbergueDao albergueDao;
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

    	reservaDao = new ReservaDaoJPA();
    	reservaDao.setup(cfg);

		albergueDao = new AlbergueDaoJPA();
		albergueDao.setup(cfg);

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
	
    /* -------- NO VA LA QUERY
    @Test
    public void test06_1LeftJoin() {
        
        List<Peregrino> listaPeregrinosDia;

        log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");


        productorDatos.crearPeregrinosSueltos();
        productorDatos.grabarPersonas();
        productorDatos.crearAlbergueconReservas();
    	productorDatos.grabarAlbergues();

    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de consulta ... \n");   

        listaPeregrinosDia = personaDao.
    	
    } 	
    */
    
    @Test
    public void test06_2OuterJoin() {

        List<Albergue> listaAlbergueVacio;

        log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		productorDatos.crearAlbergueconReservas();
    	productorDatos.grabarAlbergues();
 	
    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de consulta Albergue.obtenerAlberguesSinReservas()\n");   

        //Hay dos Albergues (a2, a3) que no tienen todavia ninguna reseva asociada
        listaAlbergueVacio = albergueDao.obtenerAlberguesSinReservas();
        
        /*
         * Comparamos el CRU del albergue porque al tener la coleccion de servicios con estrategia LAZY y no tener
         * un metodo para recuperarla, no se trae el objeto "real", asi que compramos por su clave natural.
         */
        Assert.assertEquals(2, listaAlbergueVacio.size());
        Assert.assertEquals(productorDatos.a2.getCru(), listaAlbergueVacio.get(0).getCru());
        Assert.assertEquals(productorDatos.a3.getCru(), listaAlbergueVacio.get(1).getCru());

    } 		
    
    /*
     * VER QUE HACER, Funciona pero no es una subconsulta
     */
    @Test
    public void test06_3SubconsultaQuenoEs() {

        List<Reserva> listaReservasresultado;

    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

        productorDatos.crearPeregrinosSueltos();
        productorDatos.crearAlbergueconReservas();
    	productorDatos.grabarAlbergues();
 	
    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de consulta ... \n");   

        //La reserva (r0) es la unica que cuenta con un grupo de peregrinos
        listaReservasresultado = reservaDao.obtenerReservasGrupales();

        Assert.assertEquals(1, listaReservasresultado.size());
        Assert.assertEquals(productorDatos.r0, listaReservasresultado.get(0));

    } 	
    
	@Test
    public void test06_4FuncionAgregacion() {
        
        Long numeroAlbergues;

    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		productorDatos.crearAlberguesSueltos();
    	productorDatos.grabarAlbergues();
 	
    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de consulta ALbergue.obtenerAlbergueDisponiblePorCamino() \n");   

        //Obtener numero de Albergue del Camino Ingles (1)
    	numeroAlbergues = albergueDao.obtenerAlbergueDisponiblePorCamino("Camino Inglés");
        Assert.assertEquals(Long.valueOf(1), numeroAlbergues);

        //Obtener numero de ALbergues del Camino Frances (2) ya que uno no esta disponible
        numeroAlbergues = albergueDao.obtenerAlbergueDisponiblePorCamino("Camino Francés");
        Assert.assertEquals(Long.valueOf(2), numeroAlbergues);

        //Obtener ningun albergue del Camino Primitivo
        numeroAlbergues = albergueDao.obtenerAlbergueDisponiblePorCamino("Camino Primitivo");
        Assert.assertEquals(Long.valueOf(0), numeroAlbergues);

    } 	
    
}
