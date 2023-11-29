package dns.demo.jpa.controller;

import dns.demo.jpa.entities.Bug;
import dns.demo.jpa.services.BugService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("test")
@RestController
public class BugController {

    private final  BugService bugService;

    public BugController(BugService bugService) {
        this.bugService = bugService;
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

    @GetMapping("bugs-optimized-jpa")
    public List<Bug> getAllBugOptimizedJpa() {
        List<Bug> bugs = bugService.getAllBugOptimizedJpa(
                Pageable.ofSize(20).withPage(0));
        return bugs;
    }

    @GetMapping("bugs-optimized-blazed")
    public List<Bug> getAllBugOptimizedBlazed() {
        List<Bug> bugs = bugService.getAllBugOptimizedBlazed(
                Pageable.ofSize(20).withPage(0));
        return bugs;
    }
}
