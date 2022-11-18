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
import gei.id.tutelado.model.Albergue;

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

    @Test
    public void test06_1LeftJoin() {

        List<Reserva> listaReservasresultado;

    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

        productorDatos.crearPeregrinosSueltos();
        productorDatos.crearAlbergueconReservas();
    	productorDatos.grabarAlbergues();
 	
    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de consulta Reserva.obtenerReservasGrupales() \n");   

        //Las reservas (r0, r2) cuantan con grupos de peregrinos (mas de 2 personas)
        listaReservasresultado = reservaDao.obtenerReservasGrupales();

        Assert.assertEquals(2, listaReservasresultado.size());
        Assert.assertEquals(productorDatos.r0, listaReservasresultado.get(0));
        Assert.assertEquals(productorDatos.r2, listaReservasresultado.get(1));

    } 
    
    @Test
    public void test06_2OuterJoin() {

        List<Albergue> listaAlbergueVacio;

        log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

        productorDatos.crearPeregrinosSueltos();
		productorDatos.crearAlbergueconReservas();
    	productorDatos.grabarAlbergues();
 	
    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de consulta Albergue.obtenerAlberguesSinReservas()\n");   

        //Hay dos Albergues (a2, a3) que no tienen todavia ninguna reseva asociada
        listaAlbergueVacio = albergueDao.obtenerAlberguesSinReservas();
        
        Assert.assertEquals(2, listaAlbergueVacio.size());
        Assert.assertEquals(productorDatos.a2, listaAlbergueVacio.get(0));
        Assert.assertEquals(productorDatos.a3, listaAlbergueVacio.get(1));

    } 

    @Test
    public void test06_3Subconsulta() {

        Reserva reservaResultado;

    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");
        
        productorDatos.crearPeregrinosSueltos();
        productorDatos.crearAlbergueconReservas();
    	productorDatos.grabarAlbergues();
 	
    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de consulta Reserva.obtenerReservaMayorGrupoPeregrinos() \n");   

        //La reserva (r2) cuenta con el grupo mas numeroso de un total de 3 peregrinos
        reservaResultado= reservaDao.obtenerReservaMayorGrupoPeregrinos();

        Assert.assertEquals(productorDatos.r2, reservaResultado);

    } 	
    
	@Test
    public void test06_4FuncionAgregacion() {
        
        int numeroAlbergues;

    	log.info("");	
		log.info("Configurando situación de partida del test -----------------------------------------------------------------------");

		productorDatos.crearAlberguesSueltos();
    	productorDatos.grabarAlbergues();
 	
    	log.info("");	
		log.info("Inicio del test --------------------------------------------------------------------------------------------------");
    	log.info("Objetivo: Prueba de consulta Albergue.obtenerAlbergueDisponiblePorCamino() \n");   

        //Obtener numero de Albergue del Camino Ingles (1)
    	numeroAlbergues = albergueDao.obtenerAlbergueDisponiblePorCamino("Camino Inglés");
        Assert.assertEquals(1, numeroAlbergues);

        //Obtener numero de ALbergues del Camino Frances (2) ya que uno no esta disponible
        numeroAlbergues = albergueDao.obtenerAlbergueDisponiblePorCamino("Camino Francés");
        Assert.assertEquals(2, numeroAlbergues);

        //Obtener ningun albergue del Camino Primitivo
        numeroAlbergues = albergueDao.obtenerAlbergueDisponiblePorCamino("Camino Primitivo");
        Assert.assertEquals(0, numeroAlbergues);

    } 	
    
}
