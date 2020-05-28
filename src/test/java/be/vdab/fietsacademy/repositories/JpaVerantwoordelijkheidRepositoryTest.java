package be.vdab.fietsacademy.repositories;

import be.vdab.fietsacademy.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Import(JpaVerantwoordelijkheidRepository.class)
@Sql("/insertCampus.sql")
@Sql("/insertVerantwoordelijkheid.sql")
@Sql("/insertDocent.sql")
@Sql("/insertDocentVerantwoordelijkheid.sql")
class JpaVerantwoordelijkheidRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static final String VERANTWOORDELIJKHEDEN = "verantwoordelijkheden";
    private final JpaVerantwoordelijkheidRepository repository;
    private final EntityManager manager;
    private Verantwoordelijkheid verantwoordelijkheid1;
    private Campus campus1;
    private Docent docent1;

    public JpaVerantwoordelijkheidRepositoryTest(JpaVerantwoordelijkheidRepository repository, EntityManager manager) {
        this.repository = repository;
        this.manager = manager;
    }

    @BeforeEach
    void beforeEach(){
        campus1 = new Campus("test", new Adres("test", "test", "test", "test"));
        verantwoordelijkheid1 = new Verantwoordelijkheid("Admin");
        docent1 = new Docent("test", "test", BigDecimal.TEN, "test1@test.be", Geslacht.VROUW, campus1);
    }

    private long idVanTestVerantwoordelijkheid(){
        return super.jdbcTemplate.queryForObject("select id from verantwoordelijkheden where naam = 'test'", Long.class);
    }

    @Test
    void findById(){
        assertThat(repository.findById(idVanTestVerantwoordelijkheid()).get().getNaam()).isEqualTo("test");
    }

    @Test
    void findByOnbestaandeIdMislukt(){
        assertThat(repository.findById(-1)).isNotPresent();
    }

    @Test
    void create(){
        Verantwoordelijkheid verantwoordelijkheid = new Verantwoordelijkheid("Lesgeven");
        repository.create(verantwoordelijkheid);
        manager.flush();
        assertThat(super.countRowsInTableWhere(VERANTWOORDELIJKHEDEN, "naam = 'Lesgeven'")).isOne();
    }

    @Test
    void docentenLezen(){
        assertThat(repository.findById(idVanTestVerantwoordelijkheid()).get().getDocenten())
                .extracting(docent -> docent.getVoornaam()).containsOnly("testM");
    }

    @Test
    void docentenToevoegen(){
        manager.persist(campus1);
        manager.persist(docent1);
        manager.persist(verantwoordelijkheid1);
        verantwoordelijkheid1.add(docent1);
        manager.flush();
        assertThat(repository.findById(verantwoordelijkheid1.getId()).get().getDocenten())
                .extracting(docent -> docent.getVoornaam()).containsOnly("test");
    }
}