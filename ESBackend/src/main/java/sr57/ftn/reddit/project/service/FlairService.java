package sr57.ftn.reddit.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sr57.ftn.reddit.project.model.entity.Flair;
import sr57.ftn.reddit.project.repository.FlairRepository;

import java.util.List;

@Service
public class FlairService {
    final FlairRepository flairRepository;

    @Autowired
    public FlairService(FlairRepository flairRepository) {
        this.flairRepository = flairRepository;
    }

    public Flair findOne(Integer id) {
        return flairRepository.findById(id).orElseGet(null);
    }

    public List<Flair> findAll() {
        return flairRepository.findAll();
    }

    public void remove(Integer id) {
        flairRepository.deleteById(id);
    }

    public Flair save(Flair flair) {
        return flairRepository.save(flair);
    }

    public List<Flair> findFlairsByCommunityId(Integer community_id) {
        return flairRepository.findFlairsByCommunityId(community_id);
    }
}
