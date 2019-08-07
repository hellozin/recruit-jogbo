package univ.study.recruitjogbo.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import univ.study.recruitjogbo.post.Post;
import univ.study.recruitjogbo.post.PostService;
import univ.study.recruitjogbo.request.PostingRequest;
import univ.study.recruitjogbo.security.JwtAuthentication;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public Post publish(@AuthenticationPrincipal JwtAuthentication authentication, @RequestBody @Valid PostingRequest request) {
        return postService.write(
                authentication.id,
                request.getCompanyName(),
                request.getRecruitType(),
                request.getDeadLine(),
                request.getReview()
        );
    }

    @GetMapping("/post/list")
    public List<Post> contents(@AuthenticationPrincipal JwtAuthentication authentication) {
        return postService.findAll();
    }

}
