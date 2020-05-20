package be.vdab.fietsacademy.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="docenten")
public class Docent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String voornaam, familienaam, emailAdres;
    private BigDecimal wedde;
    @Enumerated(EnumType.STRING)
    private Geslacht geslacht;

    public Docent() {
    }

    public Docent(String voornaam, String familienaam, String emailAdres, BigDecimal wedde, Geslacht geslacht) {
        this.voornaam = voornaam;
        this.familienaam = familienaam;
        this.emailAdres = emailAdres;
        this.wedde = wedde;
        this.geslacht = geslacht;
    }

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

    public Geslacht getGeslacht() {
        return geslacht;
    }
}
