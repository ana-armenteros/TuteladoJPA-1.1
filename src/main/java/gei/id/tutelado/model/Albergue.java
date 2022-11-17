package gei.id.tutelado.model;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.*;

@TableGenerator(name="generadorIdsAlbergues", table="tabla_ids",
pkColumnName="nombre_id", pkColumnValue="idAlbergue",
valueColumnName="ultimo_valor_id",
initialValue=0, allocationSize=1)

@NamedQueries ({
	@NamedQuery (name="Albergue.recuperaPorCru",
				 query="SELECT a FROM Albergue a where a.cru=:cru"),
	@NamedQuery (name="Albergue.obtenerAlbergueDisponiblePorCamino",
				 query="SELECT COUNT(id) FROM Albergue a WHERE a.camino=:camino AND a.disponible=:true"),
	@NamedQuery (name="Albergue.obtenerAlberguesSinReservas",
				 query="SELECT a FROM Reserva r RIGHT JOIN Albergue a ON r.fk_albergue_reserva=:a.id WHERE r.id IS NULL")
})

@Entity
public class Albergue {
    @Id
    @GeneratedValue (generator="generadorIdsAlbergues")
    private Long id;

    
    @Column(nullable = false, unique = true)
    private String cru;

    
    @Column(nullable = false, unique = false)
    private String nombre;

    @Column(nullable = false, unique = false)
    private String poblacion;

    @Column(nullable = false, unique = false)
    private String camino;

    @Column(nullable = false, unique = false)
    private String etapa;

    @Column(nullable = false, unique = false)
    private Boolean disponible;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable (name="t_albergue_servicios", joinColumns = @JoinColumn(name = "albergue_id"))
    @Column (name = "servicio") 
    private Set<String> servicios = new HashSet<String>();

    /*	Propagación automática de REMOVE:
		- Una reserva no puede existir sin que exista un albergue al que estar asociada.
		- Si se borra un albergue, deberán borrarse las reservas asociadas al mismo.

        Lado INVERSO de la asociacion
        -Dentro del mappedBy() nombe del atributo de la clase propietaria (Albergue)

        NOTA: necesitamos el @OrderBy aunque la coleccion esté definida como LAZY por si en 
        algun momento accedemos a la propiedad DENTRO de sesión, por defecto orden ASC
    */
    @OneToMany(mappedBy = "albergue", fetch = FetchType.LAZY, cascade={CascadeType.REMOVE}) 
    @OrderBy("fechaEntrada")
    private SortedSet<Reserva> reservas = new TreeSet<Reserva>();

    // Metodo de conveniencia para asegurarnos de que actualizamos ambos extremos
	public void anadirReserva(Reserva reserva) {
		if (reserva.getAlbergue() != null) 
            throw new RuntimeException ("La reserva ya ha sido añadida a este albergue");
		reserva.setAlbergue(this);
		this.reservas.add(reserva);
	}
    
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCru() {
        return this.cru;
    }

    public void setCru(String cru) {
        this.cru = cru;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPoblacion() {
        return this.poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public String getCamino() {
        return this.camino;
    }

    public void setCamino(String camino) {
        this.camino = camino;
    }

    public String getEtapa() {
        return this.etapa;
    }

    public void setEtapa(String etapa) {
        this.etapa = etapa;
    }

    public Boolean isDisponible() {
        return this.disponible;
    }

    public Boolean getDisponible() {
        return this.disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Set<String> getServicios() {
        return this.servicios;
    }

    public void setServicios(Set<String> servicios) {
        this.servicios = servicios;
    }

    public SortedSet<Reserva> getReservas() {
        return this.reservas;
    }

    public void setReservas(SortedSet<Reserva> reservas) {
        this.reservas = reservas;
    }


    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cru == null) ? 0 : cru.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Albergue other = (Albergue) obj;
		if (cru == null) {
			if (other.cru != null)
				return false;
		} else if (!cru.equals(other.cru))
			return false;
		return true;
	}

    @Override
    public String toString() {
        return "Albergue [id=" + id + ", cru=" + cru + ", nombre=" + nombre +
         ", poblacion=" + poblacion + ", camino=" + camino + ", etapa=" + etapa + 
         ", disponible=" + disponible + ", servicios=" + servicios + ", reservas=" + reservas + "]";
    }

}
