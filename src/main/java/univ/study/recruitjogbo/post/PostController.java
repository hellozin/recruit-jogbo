package univ.study.recruitjogbo.post;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import univ.study.recruitjogbo.request.PostingRequest;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/content")
    public Post publish(@RequestBody @Valid PostingRequest request) {
        return postService.write(
                request.getCompanyName(),
                request.getRecruitType(),
                request.getDeadLine(),
                request.getReview()
        );
    }

    @GetMapping("/content/list")
    public List<Post> contents() {
        return postService.findAll();
    }

}
