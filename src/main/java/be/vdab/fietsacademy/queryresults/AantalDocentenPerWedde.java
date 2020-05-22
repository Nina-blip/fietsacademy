package be.vdab.fietsacademy.queryresults;

import java.math.BigDecimal;

public class AantalDocentenPerWedde {
    private final BigDecimal wedde;
    private final long aantalDocenten;

    public AantalDocentenPerWedde(BigDecimal wedde, long aantalDocenten) {
        this.wedde = wedde;
        this.aantalDocenten = aantalDocenten;
    }

    public BigDecimal getWedde() {
        return wedde;
    }

    public long getAantalDocenten() {
        return aantalDocenten;
    }
}
