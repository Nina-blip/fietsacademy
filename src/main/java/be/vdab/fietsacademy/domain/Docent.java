package be.vdab.fietsacademy.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="docenten")
public class Docent {
    @Id
    private long id;
    private String voornaam, familienaam, emailAdres;
    private BigDecimal wedde;

    public long getId() {
        return id;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public String getFamilienaam() {
        return familienaam;
    }

    public String getEmailAdres() {
        return emailAdres;
    }

    public BigDecimal getWedde() {
        return wedde;
    }
}
