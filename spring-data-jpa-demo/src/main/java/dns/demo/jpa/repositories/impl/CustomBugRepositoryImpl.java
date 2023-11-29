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

@Slf4j
@Repository
public class CustomBugRepositoryImpl implements CustomBugRepository {

    private final EntityManager entityManager;

    public CustomBugRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * This is a demo how to deal with MultipleBagFetchException. This is an issue when loading two
     * @OneToMany relations in the same query. See https://thorben-janssen.com/fix-multiplebagfetchexception-hibernate/.
     *
     * An alternative to deal with MultipleBagFetchException is to use Set instead of List, but
     * we need to be sure the cartesian product (root rows * relation rows) is not large. Otherwise,
     * the overhead of querying the data is considerable.
     *
     * In this method we try the approach of separating the queries into two parts.
     */
    @Override
    public List<Bug> findAllWithMultipleOneToManyRelationsJpa(Pageable pageable) {
        log.info("Calling findAllWithMultipleOneToManyRelationsJpa...");
        List<Bug> bugs = entityManager.createQuery("""
                        select b from Bug b
                        join fetch b.status
                        join fetch b.reportedBy
                        left join fetch b.assignedTo
                        left join fetch b.verifiedBy
                        left join fetch b.screenshots
                        """, Bug.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        final Map<Long, Bug> mapWithTags = entityManager.createQuery("""
                        select distinct b from Bug b
                        left join fetch b.tags
                        where b in :bugs
                        """, Bug.class)
                .setParameter("bugs", bugs)
                .getResultList()
                .stream()
                .collect(Collectors.toMap(Bug::getId, Function.identity()));

        // Update the missing data
        bugs.forEach(b -> b.setTags(mapWithTags.get(b.getId()).getTags()));

        return bugs;
    }

    @Override
    public List<Bug> findAllWithMultipleOneToManyRelationsBlazePersistence(Pageable pageable) {
        throw new UnsupportedOperationException("TODO");
    }
}
