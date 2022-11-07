package gei.id.tutelado.dao;


import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Reserva;

public interface ReservaDao {
    
	void setup (Configuracion config);
	
	Reserva almacena (Reserva reserva);
	Reserva modifica (Reserva reserva);
	void elimina (Reserva reserva);
	Reserva recuperaPorCodigo (String codigo);
}
