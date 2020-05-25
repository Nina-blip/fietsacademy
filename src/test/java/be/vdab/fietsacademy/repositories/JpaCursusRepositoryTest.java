package be.vdab.fietsacademy.repositories;

import be.vdab.fietsacademy.domain.Cursus;
import be.vdab.fietsacademy.domain.GroepsCursus;
import be.vdab.fietsacademy.domain.IndividueleCursus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.persistence.EntityManager;
import java.util.Optional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import(JpaCursusRepository.class)
@Sql("/insertCursus.sql")
class JpaCursusRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static final String GROEPS_CURSUSSEN = "groepscursussen";
    private static final String INDIVIUELE_CURSUSSEN = "individuelecursussen";
    private static final LocalDate EEN_DATUM = LocalDate.of(2019, 1, 1);
    private final JpaCursusRepository repository;
    private final EntityManager manager;

    public JpaCursusRepositoryTest(JpaCursusRepository repository, EntityManager manager) {
        this.repository = repository;
        this.manager = manager;
    }

    private String idVanTestGroepsCursus(){
        return super.jdbcTemplate.queryForObject("select id from groepscursussen where naam ='testGroep'", String.class);
    }

    private String idVanIndividueleCursus(){
        return super.jdbcTemplate.queryForObject("select id from individuelecursussen where naam = 'testIndividueel'", String.class);
    }

    @Test
    void findGroepsCursusById(){
        Optional<Cursus> cursus = repository.findById(idVanTestGroepsCursus());
        assertThat(((GroepsCursus) cursus.get()).getNaam()).isEqualTo("testGroep");
    }

    @Test
    void findIndividueleCursusById(){
        Optional<Cursus> cursus = repository.findById(idVanIndividueleCursus());
        assertThat(((IndividueleCursus) cursus.get()).getNaam()).isEqualTo("testIndividueel");
    }

    @Test
    void findByOnbestaandeId(){
        assertThat(repository.findById("")).isNotPresent();
    }

    @Test
    void createGroepsCursus(){
        GroepsCursus cursus = new GroepsCursus("testGroep2", EEN_DATUM, EEN_DATUM);
        repository.create(cursus);
        manager.flush();
        assertThat(super.countRowsInTableWhere(GROEPS_CURSUSSEN, "id='" + cursus.getId() + "'")).isOne();
    }

    @Test
    void createIndividueleCursus(){
        IndividueleCursus cursus = new IndividueleCursus("testIndividueel2", 7);
        repository.create(cursus);
        manager.flush();
        assertThat(super.countRowsInTableWhere(INDIVIUELE_CURSUSSEN, "id='" + cursus.getId() + "'")).isOne();
    }
}