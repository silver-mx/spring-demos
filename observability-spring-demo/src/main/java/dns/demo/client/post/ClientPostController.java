package dns.demo.client.post;

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
public class ClientPostController {

    private final PostClient postClient;

    public ClientPostController(PostClient postClient) {
        this.postClient = postClient;
    }

    @GetMapping("")
    public List<Post> findAll() {
        log.info("Client request for findAll()");
        return postClient.findAll();
    }

    @GetMapping("/{id}")
    public Post findById(@PathVariable Integer id) {
        log.info("Client request for findById({})", id);
        return postClient.findById(id);
    }
}
