package dns.demo.jpa.repositories.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.PaginatedCriteriaBuilder;
import dns.demo.jpa.entities.Bug;
import dns.demo.jpa.entities.Screenshot;
import dns.demo.jpa.entities.Tag;
import dns.demo.jpa.entities.TagId;
import dns.demo.jpa.repositories.CustomBugRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Slf4j
@Repository
public class CustomBugRepositoryImpl implements CustomBugRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilderFactory criteriaBuilderFactory;

    public CustomBugRepositoryImpl(EntityManager entityManager, CriteriaBuilderFactory criteriaBuilderFactory) {
        this.entityManager = entityManager;
        this.criteriaBuilderFactory = criteriaBuilderFactory;
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

        final Map<Long, List<Tag>> mapWithTags = entityManager.createQuery("""
                        select b from Bug b
                        left join fetch b.tags
                        where b in :bugs
                        """, Bug.class)
                .setParameter("bugs", bugs)
                .getResultList()
                .stream()
                .collect(toMap(Bug::getId, Bug::getTags));

        // Update the missing data
        bugs.forEach(b -> b.setTags(mapWithTags.get(b.getId())));

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
        int firstResult = pageable.isUnpaged() ? 0 : (int) pageable.getOffset();
        int maxResults = pageable.isUnpaged() ? Integer.MAX_VALUE : pageable.getPageSize();
        List<Bug> bugs = entityManager.createQuery("""
                        select b from Bug b
                        join fetch b.status
                        join fetch b.reportedBy
                        left join fetch b.assignedTo
                        left join fetch b.verifiedBy
                        """, Bug.class)
                .setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .getResultList();

        // Step 2. Fetch the @OneToMany screenshot relation by using the bug ids.
        List<Long> bugIds = bugs.stream().map(Bug::getId).toList();
        Map<Long, List<Screenshot>> bugsWithScreenshotsButNoOtherRelations = entityManager.createQuery("""
                        select b from Bug b
                        left join fetch b.screenshots
                        where b.id in :bugIds
                        """, Bug.class)
                .setParameter("bugIds", bugIds)
                .getResultList()
                .stream().collect(toMap(Bug::getId, Bug::getScreenshots));

        // Add the screenshots to the initial query
        bugs.forEach(b -> b.setScreenshots(bugsWithScreenshotsButNoOtherRelations.get(b.getId())));

        return bugs;
    }

    @Override
    public Page<Bug> findAllWithMultipleOneToManyRelationsBlazePersistence(Pageable pageable) {
        int offset = pageable.isUnpaged() ? 0 : (int) pageable.getOffset();
        int pageSize = pageable.isUnpaged() ? Integer.MAX_VALUE : pageable.getPageSize();

        PaginatedCriteriaBuilder<Bug> criteriaBuilder = criteriaBuilderFactory.create(entityManager, Bug.class, "b")
                .innerJoinFetch("status", "st")
                .innerJoinFetch("reportedBy", "r")
                .leftJoinFetch("assignedTo", "a")
                .leftJoinFetch("verifiedBy", "v")
                .page(offset, pageSize)
                .withCountQuery(true)
                .withInlineCountQuery(true);

        pageable.getSort().forEach(order ->
                criteriaBuilder.orderBy(order.getProperty(), order.isAscending())
        );

        PagedList<Bug> bugsPage = criteriaBuilder.getResultList();

        List<Long> bugIds = bugsPage.stream().map(Bug::getId).toList();
        Map<Long, List<Tag>> idToTagsMap = criteriaBuilderFactory.create(entityManager, Bug.class, "b")
                .leftJoinFetch("tags", "t")
                .where("b.id").in(bugIds)
                .getResultList()
                .stream().collect(toMap(Bug::getId, Bug::getTags));

        // Set the tags in the bugs list
        bugsPage.forEach(b -> b.setTags(idToTagsMap.get(b.getId())));

        Map<Long, List<Screenshot>> idToScreenshotsMap = criteriaBuilderFactory.create(entityManager, Bug.class, "b")
                .leftJoinFetch("screenshots", "sc")
                .getResultList()
                .stream().collect(toMap(Bug::getId, Bug::getScreenshots));

        // Set the screenshots in the bugs list
        bugsPage.forEach(b -> b.setScreenshots(idToScreenshotsMap.get(b.getId())));

        return new PageImpl<>(bugsPage, pageable, bugsPage.getTotalSize());
    }

    @Override
    public Bug addTagWithPessimisticLock(Long id, String tag) {
        return addTagWithLockMode(id, tag, LockModeType.PESSIMISTIC_FORCE_INCREMENT);
    }

    @Override
    public Bug addTagWithOptimisticLock(Long id, String tag) {
        return addTagWithLockMode(id, tag, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
    }

    private Bug addTagWithLockMode(Long id, String tag, LockModeType lockModeType) {
        Bug bug = entityManager.find(Bug.class, id, lockModeType);
        TagId tagId = new TagId(bug.getId(), tag);
        bug.getTags().add(new Tag(tagId, bug));

        return bug;
    }

}
