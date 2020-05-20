package be.vdab.fietsacademy.services;

import be.vdab.fietsacademy.domain.Docent;
import be.vdab.fietsacademy.exceptions.DocentNietGevondenException;
import be.vdab.fietsacademy.repositories.DocentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
class DefaultDocentService implements DocentService{
    private final DocentRepository docentRepository;

    public DefaultDocentService(DocentRepository docentRepository) {
        this.docentRepository = docentRepository;
    }

    @Override
    @Transactional
    public void opslag(long id, BigDecimal percentage) {
        Optional<Docent> docent = docentRepository.findById(id);
        if (docent.isPresent()){
            docent.get().opslag(percentage);
        } else {
            throw new DocentNietGevondenException();
        }
    }
}
