package gei.id.tutelado.model;

import java.util.HashSet;
import java.util.Objects;
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
	@NamedQuery (name="Albergue.recuperaTodos",
				 query="SELECT a FROM Albergue a ORDER BY a.cru")
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

    @OneToMany(fetch = FetchType.LAZY, cascade = {}) 
    @JoinTable (name = "t_reservas_albergues",
            joinColumns = @JoinColumn(name = "albergue_id"),
            inverseJoinColumns = @JoinColumn(name = "reserva_id"))
    private Set<Reserva> reservas = new HashSet<Reserva>();

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

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Albergue)) {
            return false;
        }
        Albergue albergue = (Albergue) o;
        return Objects.equals(id, albergue.id) && Objects.equals(cru, albergue.cru) 
        && Objects.equals(nombre, albergue.nombre) && Objects.equals(poblacion, albergue.poblacion) 
        && Objects.equals(camino, albergue.camino) && Objects.equals(etapa, albergue.etapa) 
        && Objects.equals(disponible, albergue.disponible) && Objects.equals(servicios, albergue.servicios);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cru, nombre, poblacion, camino, etapa, disponible, servicios);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + id + "'" +
            ", cru='" + cru + "'" +
            ", nombre='" + nombre + "'" +
            ", poblacion='" + poblacion + "'" +
            ", camino='" + camino + "'" +
            ", etapa='" + etapa + "'" +
            ", disponible='" + disponible + "'" +
            ", servicios='" + servicios + "'" +
            "}";
    }

}
