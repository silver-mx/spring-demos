package dns.demo.jpa.services;

import dns.demo.jpa.entities.Bug;
import dns.demo.jpa.entities.Screenshot;
import dns.demo.jpa.repositories.BugRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BugService {

    private final BugRepository bugRepository;

    public BugService(BugRepository bugRepository) {
        this.bugRepository = bugRepository;
    }

    public Bug getBugById(Long id) {
        return bugRepository.findById(id).orElseThrow();
    }

    public List<Bug> getAllBugs() {
        return bugRepository.findAll();
    }

    public List<Bug> getAllBugOptimizedJpa(Pageable pageable) {
        return bugRepository.findAllWithMultipleOneToManyRelationsJpa(pageable);
    }

    public Page<Bug> getAllBugOptimizedBlazed(Pageable pageable) {
        return bugRepository.findAllWithMultipleOneToManyRelationsBlazePersistence(pageable);
    }

    public List<Bug> getAllBugsWithReferences() {
        return null;
    }


    /**
     * This example shows the warning:
     *
     * HHH90003004: firstResult/maxResults specified with collection fetch; applying in memory
     *
     * because the first graph ("graph.bug.with-tags") generates a cartesian product and hibernate
     * has hard time to group during the query, so it needs to be done in memory. This could be solved
     * splitting the graph into two graphs, one for the root entity and one for the tags. This
     * is similar to what it is done in other queries or blaze-persistence.
     *
     */
    public Page<Bug> findAllUsingNamedEntityGraphs(Pageable pageable) {
        List<Bug> bugsWithTags = bugRepository.findAllWithTagsBy(pageable);
        Map<Long, List<Screenshot>> idToScreenshotsMap = bugRepository.findAllWithScreenshotsBy(pageable)
                .stream().collect(Collectors.toMap(Bug::getId, Bug::getScreenshots));

        // Add screenshots to the bug list
        bugsWithTags.forEach(b -> b.setScreenshots(idToScreenshotsMap.get(b.getId())));

        return new PageImpl<>(bugsWithTags, pageable, bugRepository.count());
    }
}
