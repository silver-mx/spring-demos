package dns.demo.jpa.repositories;

import dns.demo.jpa.entities.Bug;
import dns.demo.jpa.entities.Tag;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
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

    /* We are splitting one query into two (findBugWithAllRelationsExceptTags, findBugWithTagsAndWithoutOtherRelations)
     * to deal with MultipleBagFetchException.
     * */
    @Query("select b from Bug b join fetch b.status join fetch b.reportedBy left join fetch b.assignedTo " +
            "left join fetch b.verifiedBy left join fetch b.screenshots " +
            "where b.id = :id")
    Optional<Bug> findBugWithAllRelationsExceptTags(@Param("id") Long id);

    @Query("select b from Bug b left join fetch b.tags where b.id = :id")
    Optional<Bug> findBugWithTagsAndWithoutOtherRelations(@Param("id") Long id);

    @Override
    default List<Bug> findAll() {
        return findAll(Pageable.unpaged()).getContent();
    }

    @Override
    default List<Bug> findAll(Sort sort) {
        return findAll(Pageable.unpaged(sort)).getContent();
    }

    @Override
    default Page<Bug> findAll(Pageable pageable) {
        List<Bug> bugs = findAllWithMultipleOneToManyRelationsJpa(pageable);
        long countAllEntities = count();
        return new PageImpl<>(bugs, pageable, countAllEntities);
    }

    @Override
    default Optional<Bug> findById(Long id) {
        return  findBugWithAllRelationsExceptTags(id).map(b -> {
            List<Tag> tags = findBugWithTagsAndWithoutOtherRelations(b.getId())
                    .orElseThrow(() -> new IllegalStateException("The bug must exist")).getTags();
            b.setTags(tags);
            return b;
        });
    }

    @EntityGraph(value = "graph.bug.with-tags")
    List<Bug> findAllWithTagsBy();

    @EntityGraph(value = "graph.bug.with-screenshots")
    List<Bug> findAllWithScreenshotsBy();
}
