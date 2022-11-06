package gei.id.tutelado.model;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.*;

@TableGenerator(name="generadorIdsReservas", table="tabla_ids",
pkColumnName="nombre_id", pkColumnValue="idReserva",
valueColumnName="ultimo_valor_id",
initialValue=0, allocationSize=1)

@NamedQueries ({
	@NamedQuery (name="Reserva.recuperaPorCodigo",
				 query="SELECT r FROM Reserva r where r.codigo=:codigo"),
	@NamedQuery (name="Reserva.recuperaTodos",
				 query="SELECT r FROM Reserva r ORDER BY r.codigo")
})

@Entity
public class Reserva {
    @Id
    @GeneratedValue (generator="generadorIdsReservas")
    private Long id;

    
    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false, unique = false)
    private LocalDate fechaEntrada;

    @Column(nullable = false, unique = false)
    private LocalDate fechaSalida;

    /*	Propagación automática de PERSIST:
		- Una reserva no puede existir sin que exista un albergue al que estar asociada.
		- Si se persiste una reserva, el albergue al que se asocia debe persistirse también.
    */
    @ManyToOne (fetch=FetchType.EAGER, cascade={CascadeType.PERSIST})
    @JoinColumn (nullable=false, unique=false)
    private Albergue albergue;

    /*	Propagación automática de PERSIST:
		- Una reserva no puede existir sin que exista el/los peregrinos a los que estar asociada.
		- Si se persiste una reserva, el/los peregrinos a los que se asocia deben persistirse también.
    */
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST}) 
    @JoinTable (name = "t_reservas_peregrinos",
            joinColumns = @JoinColumn(name = "reserva_id"))
    private Set<Peregrino> peregrinos = new TreeSet<Peregrino>();

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

    public Reserva id(Long id) {
        setId(id);
        return this;
    }

    public Reserva codigo(String codigo) {
        setCodigo(codigo);
        return this;
    }

    public Reserva fechaEntrada(LocalDate fechaEntrada) {
        setFechaEntrada(fechaEntrada);
        return this;
    }

    public Reserva fechaSalida(LocalDate fechaSalida) {
        setFechaSalida(fechaSalida);
        return this;
    }

    public Albergue getAlbergue() {
        return this.albergue;
    }

    public void setAlbergue(Albergue albergue) {
        this.albergue = albergue;
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
    public String toString() {
        return "[id=" + id + ", codigo=" + codigo + ", fechaEntrada=" +
            fechaEntrada + ", fechaSalida=" + fechaSalida +
            ", albergue=" + albergue + ", peregrinos=" + peregrinos + "]";
    }


}
