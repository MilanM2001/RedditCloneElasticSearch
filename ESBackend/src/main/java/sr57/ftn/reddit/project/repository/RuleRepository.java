package sr57.ftn.reddit.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sr57.ftn.reddit.project.model.entity.Rule;

import java.util.List;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Integer> {

    @Query(nativeQuery = true, value = "Select * From rule where rule.community_id = ?")
    List<Rule> findRulesByCommunityId(Integer community_id);
}
