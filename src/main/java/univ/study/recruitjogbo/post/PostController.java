package univ.study.recruitjogbo.post;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import univ.study.recruitjogbo.request.PostingRequest;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

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
