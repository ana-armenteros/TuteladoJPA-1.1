package gei.id.tutelado.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@TableGenerator(name="generadorIdsReservas", table="tabla_ids",
pkColumnName="nombre_id", pkColumnValue="idReserva",
valueColumnName="ultimo_valor_id",
initialValue=0, allocationSize=1)

@NamedQueries ({
	@NamedQuery (name="Reserva.recuperaPorCodigo",
				 query="SELECT r FROM Reserva r WHERE r.codigo=:codigo"),
    @NamedQuery (name="Reserva.recuperaTodos",
				 query="SELECT r FROM Reserva r ORDER BY r.codigo"),
	@NamedQuery (name="Reserva.obtenerReservasGrupales",
                 query="SELECT DISTINCT r FROM Reserva r INNER JOIN r.peregrinos p WHERE size(p) > 1"),
    @NamedQuery (name="Reserva.obtenerReservaMayorGrupoPeregrinos",
                 query="SELECT r FROM Reserva r INNER JOIN r.peregrinos p WHERE size(p) = (SELECT MAX(size(p)) FROM Reserva r INNER JOIN r.peregrinos p)")
})

@Entity
public class Reserva implements Comparable<Reserva>{

    @Id
    @GeneratedValue (generator="generadorIdsReservas")
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false, unique = false)
    private LocalDate fechaEntrada;

    @Column(nullable = false, unique = false)
    private LocalDate fechaSalida;

    /*
        Lado PROPIETARIO de la asociación bidireccional
        - Incluye la clave foranea que apunta a Albergue
    */
    @ManyToOne (fetch=FetchType.EAGER, cascade={})
    @JoinColumn (name= "fk_Albergue_Reserva", nullable=false, unique=false)
    private Albergue albergue;

    /*	
        Propagación automática de PERSIST:
		- Una reserva no puede existir sin que exista el/los peregrinos a los que estar asociada.
		- Si se persiste una reserva, el/los peregrinos a los que se asocia deben persistirse también.
    */
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST}) 
    @JoinTable (name = "t_reservas_peregrinos",
            joinColumns = @JoinColumn(name = "reserva_id"))
    private Set<Peregrino> peregrinos = new HashSet<Peregrino>();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getFechaEntrada() {
        return this.fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return this.fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Albergue getAlbergue() {
        return this.albergue;
    }

    public void setAlbergue(Albergue albergue) {
        this.albergue = albergue;
    }

    public Set<Peregrino> getPeregrinos() {
        return peregrinos;
    }

    public void setPeregrinos(Set<Peregrino> peregrinos) {
        this.peregrinos = peregrinos;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
		Reserva other = (Reserva) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

    @Override
	public int compareTo(Reserva other) {
		return (this.fechaEntrada.isBefore(other.getFechaEntrada())? -1:1);
	}

}
