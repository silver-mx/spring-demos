package dns.demo.jwt.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home(Principal principal) {
        return "Hello " + principal.getName() + "!!";
    }

    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping("/secured-read")
    public String securedRead() {
        return "This is a secured endpoint with read authority!!";
    }

    @PreAuthorize("hasAuthority('SCOPE_write')")
    @GetMapping("/secured-write")
    public String securedWrite() {
        return "This is a secured endpoint with write authority!!";
    }
}
