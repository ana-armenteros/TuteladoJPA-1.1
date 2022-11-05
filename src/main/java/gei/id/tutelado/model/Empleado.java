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

    public Empleado id(Long id) {
        setId(id);
        return this;
    }

    public Empleado nss(String nss) {
        setNss(nss);
        return this;
    }

    public Empleado puesto(String puesto) {
        setPuesto(puesto);
        return this;
    }

}
