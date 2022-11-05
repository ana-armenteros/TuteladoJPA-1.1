package gei.id.tutelado.model;

import java.util.Objects;

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

	public Persona id(Long id) {
		setId(id);
		return this;
	}

	public Persona nif(String nif) {
		setNif(nif);
		return this;
	}

	public Persona nombre(String nombre) {
		setNombre(nombre);
		return this;
	}

	public Persona apellido(String apellido) {
		setApellido(apellido);
		return this;
	}

	public Persona nacionalidad(String nacionalidad) {
		setNacionalidad(nacionalidad);
		return this;
	}

	public Persona telefono(String telefono) {
		setTelefono(telefono);
		return this;
	}

	public Persona email(String email) {
		setEmail(email);
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Persona)) {
			return false;
		}
		Persona persona = (Persona) o;
		return Objects.equals(id, persona.id) && Objects.equals(nif, persona.nif) && Objects.equals(nombre, persona.nombre) && Objects.equals(apellido, persona.apellido) && Objects.equals(nacionalidad, persona.nacionalidad) && Objects.equals(telefono, persona.telefono) && Objects.equals(email, persona.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nif, nombre, apellido, nacionalidad, telefono, email);
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
