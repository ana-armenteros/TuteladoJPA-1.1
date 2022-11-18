package gei.id.tutelado.dao;


import java.util.List;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Reserva;

public interface ReservaDao {
    
	void setup (Configuracion config);
	
	Reserva almacena (Reserva reserva);
	Reserva modifica (Reserva reserva);
	void elimina (Reserva reserva);
	Reserva recuperaPorCodigo (String codigo);

	//Queries para la consulta (1.LeftJoin y 3.Subconsulta) que no es subconsulta
	List<Reserva> obtenerReservasGrupales ();
	Reserva obtenerReservaMayorGrupoPeregrinos();
}
