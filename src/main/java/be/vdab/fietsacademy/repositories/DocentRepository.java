package be.vdab.fietsacademy.repositories;

import java.util.Optional;
import be.vdab.fietsacademy.domain.Docent;

public interface DocentRepository {
    Optional<Docent> findById(long id);
}
