package be.vdab.fietsacademy.repositories;

import be.vdab.fietsacademy.domain.Verantwoordelijkheid;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class JpaVerantwoordelijkheidRepository implements VerantwoordelijkheidRepository {
    private final EntityManager entityManager;

    public JpaVerantwoordelijkheidRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Verantwoordelijkheid> findById(long id) {
        return Optional.ofNullable(entityManager.find(Verantwoordelijkheid.class, id));
    }

    @Override
    public void create(Verantwoordelijkheid verantwoordelijkheid) {
        entityManager.persist(verantwoordelijkheid);
    }
}
