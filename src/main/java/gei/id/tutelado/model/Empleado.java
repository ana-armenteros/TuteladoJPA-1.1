package gei.id.tutelado.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Empleado extends Persona {
    @Column(nullable = false, unique = true)
    private String nss;

    @Column(nullable = false, unique=false)
	private String puesto;

    public String getNss() {
        return this.nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getPuesto() {
        return this.puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((nss == null) ? 0 : nss.hashCode());
        result = prime * result + ((puesto == null) ? 0 : puesto.hashCode());
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
        Empleado other = (Empleado) obj;
        if (nss == null) {
            if (other.nss != null)
                return false;
        } else if (!nss.equals(other.nss))
            return false;
        if (puesto == null) {
            if (other.puesto != null)
                return false;
        } else if (!puesto.equals(other.puesto))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + "is a Empleado [nss=" + nss + ", puesto=" + puesto + "]";
    }

}
