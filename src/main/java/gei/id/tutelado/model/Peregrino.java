package gei.id.tutelado.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/*@NamedQueries ({
	@NamedQuery (name="Peregrino.obtenerPeregrinosReservaEnFechaAlbergue",
				 query="SELECT p FROM Reserva r" +
                       "JOIN t_reservas_peregrinos rp ON rp.reserva_id=:id" +
                       "JOIN Peregrino p ON p.id=:rp.peregrinos_id" + 
                       "JOIN Albergue a ON r.fk_albergue_reserva=:a.id" + 
                       "WHERE (dia BETWEEN fechaentrada AND fechasalida) AND (a.nombre=:nombre")
})*/

@Entity
public class Peregrino extends Persona {
    @Column(nullable = false, unique=false)
	private String medio;

    @Column(nullable = false, unique=false)
	private Boolean limitacionFisica;

    public String getMedio() {
        return this.medio;
    }

    public void setMedio(String medio) {
        this.medio = medio;
    }

    public Boolean isLimitacionFisica() {
        return this.limitacionFisica;
    }

    public Boolean getLimitacionFisica() {
        return this.limitacionFisica;
    }

    public void setLimitacionFisica(Boolean limitacionFisica) {
        this.limitacionFisica = limitacionFisica;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((medio == null) ? 0 : medio.hashCode());
        result = prime * result + ((limitacionFisica == null) ? 0 : limitacionFisica.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Peregrino other = (Peregrino) obj;
        if (medio == null) {
            if (other.medio != null)
                return false;
        } else if (!medio.equals(other.medio))
            return false;
        if (limitacionFisica == null) {
            if (other.limitacionFisica != null)
                return false;
        } else if (!limitacionFisica.equals(other.limitacionFisica))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + "is a Peregrino [medio=" + medio + ", limitacionFisica=" + limitacionFisica + "]";
    }

}
