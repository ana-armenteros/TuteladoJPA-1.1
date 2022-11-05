package gei.id.tutelado.model;

import javax.persistence.Column;
import javax.persistence.Entity;


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

}
