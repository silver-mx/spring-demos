package dns.demo.jpa.repositories.impl;

import dns.demo.jpa.entities.Bug;
import dns.demo.jpa.repositories.CustomBugRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Slf4j
@Repository
public class CustomBugRepositoryImpl implements CustomBugRepository {

    private final EntityManager entityManager;

    public CustomBugRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * This is a demo how to deal with MultipleBagFetchException. This is an issue when loading two
     *
     * @OneToMany relations in the same query. See https://thorben-janssen.com/fix-multiplebagfetchexception-hibernate/.
     * <p>
     * An alternative to deal with MultipleBagFetchException is to use Set instead of List, but
     * we need to be sure the cartesian product (root rows * relation rows) is not large. Otherwise,
     * the overhead of querying the data is considerable.
     * <p>
     * In this method we try the approach of separating the queries into two parts.
     */
    @Override
    public List<Bug> findAllWithMultipleOneToManyRelationsJpa(Pageable pageable) {
        log.info("Calling findAllWithMultipleOneToManyRelationsJpa...");
        List<Bug> bugs = getAllBugsWithoutTags(pageable);

        final Map<Long, Bug> mapWithTags = entityManager.createQuery("""
                        select distinct b from Bug b
                        left join fetch b.tags
                        where b in :bugs
                        """, Bug.class)
                .setParameter("bugs", bugs)
                .getResultList()
                .stream()
                .collect(toMap(Bug::getId, Function.identity()));

        // Update the missing data
        bugs.forEach(b -> b.setTags(mapWithTags.get(b.getId()).getTags()));

        return bugs;
    }


    /* This method splits a single query into two to deal with
     * HHH90003004: firstResult/maxResults specified with collection fetch; applying in memory
     *  */
    private List<Bug> getAllBugsWithoutTags(Pageable pageable) {
        /*
        ORIGINAL QUERY => return entityManager.createQuery("""
                        select b from Bug b
                        join fetch b.status
                        join fetch b.reportedBy
                        left join fetch b.assignedTo
                        left join fetch b.verifiedBy
                        left join fetch b.screenshots
                        """, Bug.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();*/

        // Step 1. Fetch all the bugs without @OneToMany relations.
        List<Bug> bugs = entityManager.createQuery("""
                        select b from Bug b
                        join fetch b.status
                        join fetch b.reportedBy
                        left join fetch b.assignedTo
                        left join fetch b.verifiedBy
                        """, Bug.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // Step 2. Fetch the @OneToMany screenshot relation by using the bug ids.
        List<Long> bugIds = bugs.stream().map(Bug::getId).toList();
        Map<Long, Bug> bugsWithScreenshotsButNoOtherRelations = entityManager.createQuery("""
                        select b from Bug b
                        left join fetch b.screenshots
                        where b.id in :bugIds
                        """, Bug.class)
                .setParameter("bugIds", bugIds)
                .getResultList()
                .stream()
                .collect(toMap(Bug::getId, Function.identity()));

        // Add the screenshots to the initial query
        bugs.forEach(b -> b.setScreenshots(bugsWithScreenshotsButNoOtherRelations.get(b.getId()).getScreenshots()));

        return bugs;
    }

    @Override
    public List<Bug> findAllWithMultipleOneToManyRelationsBlazePersistence(Pageable pageable) {
        throw new UnsupportedOperationException("TODO");
    }
}
