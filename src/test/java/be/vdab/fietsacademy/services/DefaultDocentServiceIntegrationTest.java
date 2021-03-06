package be.vdab.fietsacademy.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

@DataJpaTest
@Import(DefaultDocentService.class)
@ComponentScan(value = "be.vdab.fietsacademy.repositories",
resourcePattern = "JpaDocentRepository.class")
@Sql("/insertCampus.sql")
@Sql("/insertDocent.sql")
class DefaultDocentServiceIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final DefaultDocentService service;
    private final EntityManager manager;

    public DefaultDocentServiceIntegrationTest(DefaultDocentService service, EntityManager manager) {
        this.service = service;
        this.manager = manager;
    }

    private long idVanTestMan() {
        return super.jdbcTemplate.queryForObject(
                "select id from docenten where voornaam='testM'", Long.class);
    }

    @Test
    void opslag(){
        long id = idVanTestMan();
        service.opslag(id, BigDecimal.TEN);
        manager.flush();
        assertThat(super.jdbcTemplate.queryForObject("select wedde from docenten where id=?", BigDecimal.class, id)).isEqualByComparingTo("1100");
    }
}
