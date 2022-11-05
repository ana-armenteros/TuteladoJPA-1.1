package gei.id.tutelado.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
    @GeneratedValue (generator="generadorIdsCodigos")
    private Long id;

    
    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false, unique = false)
    private LocalDate fechaEntrada;

    @Column(nullable = false, unique = false)
    private LocalDate fechaSalida;

    @ManyToOne (cascade={}, fetch=FetchType.EAGER)
    @JoinColumn (nullable=false, unique=false)
    private Albergue albergue;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {}) 
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

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Reserva)) {
            return false;
        }
        Reserva reserva = (Reserva) o;
        return Objects.equals(id, reserva.id) && Objects.equals(codigo, reserva.codigo) 
        && Objects.equals(fechaEntrada, reserva.fechaEntrada) 
        && Objects.equals(fechaSalida, reserva.fechaSalida);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigo, fechaEntrada, fechaSalida);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", codigo='" + getCodigo() + "'" +
            ", fechaEntrada='" + getFechaEntrada() + "'" +
            ", fechaSalida='" + getFechaSalida() + "'" +
            "}";
    }

}
