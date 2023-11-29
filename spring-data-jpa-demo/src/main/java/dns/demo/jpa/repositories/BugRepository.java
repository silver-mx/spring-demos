package dns.demo.jpa.repositories;

import dns.demo.jpa.entities.Bug;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
//@RepositoryRestResource(collectionResourceRel = "bugs", path = "bugs")
public interface BugRepository extends JpaRepository<Bug, Long>, CustomBugRepository {

    /**
     * These are 2 examples how to load lazy relations in JPA. This can be useful when working with Spring Data REST.
     */
    @Query(value = "SELECT bug FROM Bug bug " +
            "LEFT JOIN FETCH bug.reportedBy " +
            "LEFT JOIN FETCH bug.assignedTo " +
            "LEFT JOIN FETCH bug.verifiedBy " +
            "LEFT JOIN FETCH bug.tags " +
            "LEFT JOIN FETCH bug.screenshots",
            countQuery = "SELECT COUNT(bug) FROM Bug bug")
    Page<Bug> findAllBugsWithLazyRelations(Pageable pageable);

    @Query("SELECT bug FROM Bug bug JOIN FETCH bug.reportedBy " +
            "LEFT JOIN FETCH bug.assignedTo " +
            "LEFT JOIN FETCH bug.verifiedBy " +
            "LEFT JOIN FETCH bug.tags " +
            "LEFT JOIN FETCH bug.screenshots " +
            "WHERE bug.id = :id")
    Optional<Bug> findBugWithLazyRelations(@Param("id") Long id);

    @Override
    default List<Bug> findAll() {
        return findAllBugsWithLazyRelations(Pageable.unpaged()).getContent();
    }

    @Override
    default List<Bug> findAll(Sort sort) {
        return findAllBugsWithLazyRelations(Pageable.unpaged(sort)).getContent();
    }

    @Override
    default Page<Bug> findAll(Pageable pageable) {
        return findAllBugsWithLazyRelations(pageable);
    }

    @Override
    default Optional<Bug> findById(Long id) {
        return findBugWithLazyRelations(id);
    }
}
