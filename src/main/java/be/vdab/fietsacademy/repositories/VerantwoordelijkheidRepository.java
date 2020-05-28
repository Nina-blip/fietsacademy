package be.vdab.fietsacademy.repositories;

import java.util.Optional;
import be.vdab.fietsacademy.domain.Verantwoordelijkheid;

public interface VerantwoordelijkheidRepository {
    Optional<Verantwoordelijkheid> findById(long id);
    void create(Verantwoordelijkheid verantwoordelijkheid);
}
