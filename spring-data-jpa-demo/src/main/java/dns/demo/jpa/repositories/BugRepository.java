package dns.demo.jpa.repositories;

import dns.demo.jpa.entities.Bug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "bugs", path = "bugs")
public interface BugRepository extends JpaRepository<Bug, Long> {

}
