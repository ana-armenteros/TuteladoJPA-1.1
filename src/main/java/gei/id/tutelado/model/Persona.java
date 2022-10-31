package gei.id.tutelado.model;

import javax.persistence.*;

@TableGenerator(name="generadorIdsPersonas", table="tabla_ids",
pkColumnName="nombre_id", pkColumnValue="idPersona",
valueColumnName="ultimo_valor_id",
initialValue=0, allocationSize=1)

@NamedQueries ({
	@NamedQuery (name="Persona.recuperaPorNif",
				 query="SELECT p FROM Persona p where p.nif=:nif"),
	@NamedQuery (name="Persona.recuperaTodos",
				 query="SELECT p FROM Persona p ORDER BY p.nif")
})

@Entity
/*	Tabla por clase concreta:
		- Se creará una tabla por cada subclase, cada una con los atributos de la superclase.
		- Las subclases tienen atributos obligatorios -> se descarta tabla por jerarquía.
*/
@Inheritance (strategy = InheritanceType.TABLE_PER_CLASS)
public class Persona {
    @Id
    @GeneratedValue (generator="generadorIdsPersonas")
    private Long id;

    @Column(nullable = false, unique = true)
    private String nif;

    @Column(nullable = false, unique=false)
	private String nombre;

	@Column(nullable = false, unique=false)
	private String apellido;

	@Column(nullable = false, unique=false)
    private String nacionalidad;

	@Column(nullable = false, unique=false)
    private String telefono;

    @Column(nullable = true, unique=false)
    private String email;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNif() {
		return this.nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getNacionalidad() {
		return this.nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nif == null) ? 0 : nif.hashCode());
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
		Persona other = (Persona) obj;
		if (nif == null) {
			if (other.nif != null)
				return false;
		} else if (!nif.equals(other.nif))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "{" +
			" id='" + getId() + "'" +
			", nif='" + getNif() + "'" +
			", nombre='" + getNombre() + "'" +
			", apellido='" + getApellido() + "'" +
			", nacionalidad='" + getNacionalidad() + "'" +
			", telefono='" + getTelefono() + "'" +
			", email='" + getEmail() + "'" +
			"}";
	}
}
