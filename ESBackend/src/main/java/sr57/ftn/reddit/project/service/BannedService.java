package sr57.ftn.reddit.project.service;

import org.springframework.stereotype.Service;
import sr57.ftn.reddit.project.model.entity.Banned;
import sr57.ftn.reddit.project.repository.BannedRepository;

import java.util.List;

@Service
public class BannedService {
    final private BannedRepository bannedRepository;

    public BannedService(BannedRepository bannedRepository) {
        this.bannedRepository = bannedRepository;
    }

    public Banned findOne(Integer id) {
        return bannedRepository.findById(id).orElseGet(null);
    }

    public List<Banned> findAll() {
        return bannedRepository.findAll();
    }

    public void remove(Integer id) {
        bannedRepository.deleteById(id);
    }

    public Banned save(Banned banned) {
        return bannedRepository.save(banned);
    }
}
