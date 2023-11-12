package dns.demo.post;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/posts")
@RestController
public class PostController {

    private final JsonPlaceHolderService jsonPlaceHolderService;

    public PostController(JsonPlaceHolderService jsonPlaceHolderService) {
        this.jsonPlaceHolderService = jsonPlaceHolderService;
    }

    @GetMapping("")
    public List<Post> findAll() {
        return jsonPlaceHolderService.findAll();
    }

    @GetMapping("/{id}")

    public Post findById(@PathVariable Integer id) {
        return jsonPlaceHolderService.findById(id);
    }
}
