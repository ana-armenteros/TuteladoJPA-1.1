package gei.id.tutelado.dao;


import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Albergue;

public interface AlbergueDao {
    
	void setup (Configuracion config);
	
	Albergue almacena (Albergue albergue);
	Albergue modifica (Albergue albergue);
	void elimina (Albergue albergue);
	Albergue recuperaPorCru (String cru);

	/* 
		Operaciones por atributos LAZY
		- Recibe un Albergue con la coleccion de Reservas como proxy SIN INICIALIZAR
		- Devuelve una copia del Albergergue con la coleccion de Reservas INICIALIZADA
	*/
	Albergue restauraReservas (Albergue albergue);

}
