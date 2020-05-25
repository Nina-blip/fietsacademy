package be.vdab.fietsacademy.repositories;

import be.vdab.fietsacademy.domain.Campus;

import java.util.Optional;

public interface CampusRepository {
    Optional<Campus> findById(long id);
    void create (Campus campus);
}
