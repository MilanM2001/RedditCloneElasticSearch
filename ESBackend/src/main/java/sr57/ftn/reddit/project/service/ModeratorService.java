package sr57.ftn.reddit.project.service;

import org.springframework.stereotype.Service;
import sr57.ftn.reddit.project.model.entity.Moderator;
import sr57.ftn.reddit.project.repository.ModeratorRepository;

@Service
public class ModeratorService {

    final ModeratorRepository moderatorRepository;

    public ModeratorService(ModeratorRepository moderatorRepository) {
        this.moderatorRepository = moderatorRepository;
    }

    public Moderator save(Moderator moderator) {
        return this.moderatorRepository.save(moderator);
    }

    public void deleteByCommunityId(Integer community_id) {
        moderatorRepository.deleteByCommunityId(community_id);
    }
}
