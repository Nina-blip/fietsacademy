package be.vdab.fietsacademy.repositories;

import be.vdab.fietsacademy.domain.Adres;
import be.vdab.fietsacademy.domain.Campus;
import be.vdab.fietsacademy.domain.Docent;
import be.vdab.fietsacademy.domain.Geslacht;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JpaDocentRepository.class)
@Sql("/insertCampus.sql")
@Sql("/insertDocent.sql")
class JpaDocentRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final JpaDocentRepository repository;
    private static final String DOCENTEN = "docenten";
    private Docent docent;
    private final EntityManager manager;
    private Campus campus;

    public JpaDocentRepositoryTest(JpaDocentRepository repository, EntityManager manager) {
        this.repository = repository;
        this.manager = manager;
    }

    private long idVanTestMan() {
        return super.jdbcTemplate.queryForObject("select id from docenten where voornaam= 'testM'", Long.class);
    }

    private long idVanTestVrouw() {
        return super.jdbcTemplate.queryForObject("select id from docenten where voornaam= 'testV'", Long.class);
    }

    @BeforeEach
    void beforeEach() {
        campus = new Campus("test", new Adres("test", "test", "test", "test"));
        docent = new Docent("test", "test", BigDecimal.TEN, "test@test.be", Geslacht.MAN, campus);
    }

    @Test
    void findById() {
        assertThat(repository.findById(idVanTestMan()).get().getVoornaam()).isEqualTo("testM");
    }

    @Test
    void findByOnbestaandeId() {
        assertThat(repository.findById(-1)).isNotPresent();
    }

    @Test
    void man() {
        assertThat(repository.findById(idVanTestMan()).get().getGeslacht()).isEqualTo(Geslacht.MAN);
    }

    @Test
    void vrouw() {
        assertThat(repository.findById(idVanTestVrouw()).get().getGeslacht()).isEqualTo(Geslacht.VROUW);
    }

    @Test
    void create() {
        manager.persist(campus);
        repository.create(docent);
        assertThat(docent.getId()).isPositive();
        assertThat(super.countRowsInTableWhere(DOCENTEN, "id=" + docent.getId())).isOne();
        assertThat(super.jdbcTemplate.queryForObject("select campusid from docenten where id=?", Long.class, docent.getId())).isEqualTo(campus.getId());
    }

    @Test
    void delete() {
        long id = idVanTestMan();
        repository.delete(id);
        manager.flush();
        assertThat(super.countRowsInTableWhere(DOCENTEN, "id=" + id)).isZero();
    }

    @Test
    void findAll() {
        assertThat(repository.findAll()).hasSize(super.countRowsInTable(DOCENTEN))
                .extracting(docent -> docent.getWedde()).isSorted();
    }

    @Test
    void findByWeddeBetween() {
        BigDecimal duizend = BigDecimal.valueOf(1000);
        BigDecimal tweeduizend = BigDecimal.valueOf(2000);
        List<Docent> docenten = repository.findByWeddeBetween(duizend, tweeduizend);
        assertThat(docenten).hasSize(super.countRowsInTableWhere(DOCENTEN, "wedde between 1000 and 2000"))
                .allSatisfy(docent -> assertThat(docent.getWedde()).isBetween(duizend, tweeduizend));
    }

    @Test
    void findEmailAdressen() {
        assertThat(repository.findEmailAdressen()).hasSize(super.jdbcTemplate.queryForObject("select count(emailAdres) from docenten", Integer.class))
                .allSatisfy(adres -> assertThat(adres).contains("@"));
    }

    @Test
    void findIdsEnEmailAdressen() {
        assertThat(repository.findIdsEnEmailAdressen()).hasSize(super.countRowsInTable(DOCENTEN));
    }

    @Test
    void findGrootsteWedde() {
        assertThat(repository.findGrootsteWedde()).isEqualByComparingTo(
                super.jdbcTemplate.queryForObject("select max(wedde) from docenten", BigDecimal.class));
    }

    @Test
    void findAantalDocentenPerWedde() {
        BigDecimal duizend = BigDecimal.valueOf(1000);
        assertThat(repository.findAantalDocentenPerWedde()).hasSize(
                super.jdbcTemplate.queryForObject("select count(distinct wedde) from docenten", Integer.class))
                .filteredOn(aantalPerWedde -> aantalPerWedde.getWedde().compareTo(duizend) == 0)
                .allSatisfy(aantalPerWedde -> assertThat(aantalPerWedde.getAantalDocenten())
                        .isEqualTo(super.countRowsInTableWhere(DOCENTEN, "wedde = 1000")));
    }

    @Test
    void algemeneOpslag() {
        assertThat(repository.algemeneOpslag(BigDecimal.TEN)).isEqualTo(super.countRowsInTable(DOCENTEN));
        assertThat(super.jdbcTemplate.queryForObject("select wedde from docenten where id=?",
                BigDecimal.class, idVanTestMan())).isEqualByComparingTo("1100");
    }

    @Test
    void eenBijnaamLezen() {
        assertThat(repository.findById(idVanTestMan()).get().getBijnamen()).containsOnly("test");
    }

    @Test
    void bijnaamToevoegen() {
        manager.persist(campus);
        repository.create(docent);
        docent.addBijnaam("test");
        manager.flush();
        assertThat(super.jdbcTemplate.queryForObject("select bijnaam from docentenbijnamen where docentid=?", String.class, docent.getId())).isEqualTo("test");
    }

    @Test
    void campusLazyLoaded(){
        Docent docent = repository.findById(idVanTestMan()).get();
        assertThat(docent.getCampus().getNaam()).isEqualTo("test");
    }
}