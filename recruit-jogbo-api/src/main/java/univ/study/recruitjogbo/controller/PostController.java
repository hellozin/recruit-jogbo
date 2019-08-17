package univ.study.recruitjogbo.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import univ.study.recruitjogbo.post.Post;
import univ.study.recruitjogbo.post.PostService;
import univ.study.recruitjogbo.post.PostSpecs;
import univ.study.recruitjogbo.post.SearchRequest;
import univ.study.recruitjogbo.request.PostingRequest;
import univ.study.recruitjogbo.security.JwtAuthentication;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    private final PagedResourcesAssembler<Post> assembler;

    @PostMapping("/post")
    public Post publish(@AuthenticationPrincipal JwtAuthentication authentication,
                        @RequestBody @Valid PostingRequest request) {
        return postService.write(
                authentication.id,
                request.getCompanyName(),
                request.getRecruitType(),
                request.getDeadLine(),
                request.getReview()
        );
    }

    @GetMapping("/post/list")
    public PagedResources<Resource<Post>> contents(@AuthenticationPrincipal JwtAuthentication authentication,
                                                   @RequestParam(required = false) SearchRequest request,
                                                   Pageable pageable) {
        return assembler.toResource(postService.findAll(PostSpecs.searchWith(request), pageable));
    }

}
