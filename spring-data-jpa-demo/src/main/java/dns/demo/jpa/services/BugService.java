package dns.demo.jpa.services;

import dns.demo.jpa.entities.Bug;
import dns.demo.jpa.entities.Screenshot;
import dns.demo.jpa.repositories.BugRepository;
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

    public List<Bug> getAllBugOptimizedBlazed(Pageable pageable) {
        return bugRepository.findAllWithMultipleOneToManyRelationsBlazePersistence(pageable);
    }

    public List<Bug> getAllBugsWithReferences() {
        return null;
    }

    public List<Bug> findAllUsingNamedEntityGraphs(Pageable pageable) {
        List<Bug> bugsWithTags = bugRepository.findAllWithTagsBy();
        Map<Long, List<Screenshot>> idToScreenshotsMap = bugRepository.findAllWithScreenshotsBy().stream().collect(Collectors.toMap(Bug::getId, Bug::getScreenshots));

        // Add screenshots to the bug list
        bugsWithTags.forEach(b -> b.setScreenshots(idToScreenshotsMap.get(b.getId())));

        return bugsWithTags;
    }
}
