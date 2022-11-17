package gei.id.tutelado.dao;


import java.time.LocalDate;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Peregrino;
import gei.id.tutelado.model.Persona;

public interface PersonaDao {
    
	void setup (Configuracion config);
	
	Persona almacena (Persona persona);
	Persona modifica (Persona persona);
	void elimina (Persona persona);
	Persona recuperaPorNif (String nif);
	Peregrino obtenerPeregrinosReservaEnFechaAlbergue(LocalDate dia, String nombre);
}
