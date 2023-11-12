package dns.demo.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/home")
@RestController
public class ServerController {

    @GetMapping
    public String home() {
        return "Server Alive!!";
    }


}
