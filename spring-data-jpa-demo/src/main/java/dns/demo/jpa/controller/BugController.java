package dns.demo.jpa.controller;

import dns.demo.jpa.dto.BugDto;
import dns.demo.jpa.entities.Bug;
import dns.demo.jpa.mapper.BugMapper;
import dns.demo.jpa.services.BugService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("test")
@RestController
public class BugController {

    private final BugService bugService;
    private final BugMapper bugMapper;

    public BugController(BugService bugService,
                         BugMapper bugMapper) {
        this.bugService = bugService;
        this.bugMapper = bugMapper;
    }

    @GetMapping("bugs/{id}")
    public Bug getBugById(@PathVariable("id") Long id) {
        Bug bug = bugService.getBugById(id);
        return bug;
    }

    @GetMapping("bugs")
    public List<Bug> getAllBugs() {
        List<Bug> bugs = bugService.getAllBugs();
        return bugs;
    }

    @GetMapping("bugs-optimized-jpa-dto")
    public List<BugDto> getAllBugOptimizedJpaDto() {
        return bugService.getAllBugOptimizedJpa(Pageable.ofSize(20).withPage(0))
                .stream().map(bugMapper::toDto).toList();
    }

    @GetMapping("bugs-optimized-jpa")
    public List<Bug> getAllBugOptimizedJpa() {
        List<Bug> bugs = bugService.getAllBugOptimizedJpa(
                Pageable.ofSize(20).withPage(0));
        return bugs;
    }

    @GetMapping("bugs-optimized-blazed")
    public Page<Bug> getAllBugOptimizedBlazed(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                              @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        Page<Bug> bugs = bugService.getAllBugOptimizedBlazed(
                PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "id")));
        return bugs;
    }

    @GetMapping("bugs-entity-graph")
    public Page<Bug> findAllUsingNamedEntityGraphs(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                   @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        Page<Bug> bugs = bugService.findAllUsingNamedEntityGraphs(PageRequest.of(pageNumber, pageSize));
        return bugs;
    }

    @PatchMapping("add-tag-pessimistic-lock/{id}/{tag}")
    public Bug addTagWithPessimisticLock(@PathVariable("id") Long id, @PathVariable("tag") String tag) {
        Bug updatedBug = bugService.addTagWithPessimisticLock(id, tag);
        return bugService.getBugById(updatedBug.getId());
    }

    @PatchMapping("add-tag-optimistic-lock/{id}/{tag}")
    public Bug addTagWithOptimisticLock(@PathVariable("id") Long id, @PathVariable("tag") String tag) {
        Bug updatedBug = bugService.addTagWithOptimisticLock(id, tag);
        return bugService.getBugById(updatedBug.getId());
    }
}
