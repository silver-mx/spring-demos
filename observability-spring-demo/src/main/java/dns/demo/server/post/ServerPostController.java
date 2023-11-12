package dns.demo.server.post;

import dns.demo.common.post.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/api/posts")
@RestController
public class ServerPostController {

    private final JsonPlaceHolderClient jsonPlaceHolderClient;

    public ServerPostController(JsonPlaceHolderClient jsonPlaceHolderClient) {
        this.jsonPlaceHolderClient = jsonPlaceHolderClient;
    }

    @GetMapping("")
    public List<Post> findAll() {
        log.info("Server request for findAll()");
        return jsonPlaceHolderClient.findAll();
    }

    @GetMapping("/{id}")
    public Post findById(@PathVariable Integer id) {
        log.info("Server request for findById({})", id);
        return jsonPlaceHolderClient.findById(id);
    }
}
