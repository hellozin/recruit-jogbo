package univ.study.recruitjogbo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import univ.study.recruitjogbo.post.Post;
import univ.study.recruitjogbo.post.PostService;
import univ.study.recruitjogbo.post.PostSpecs;
import univ.study.recruitjogbo.request.PostingRequest;
import univ.study.recruitjogbo.request.SearchRequest;
import univ.study.recruitjogbo.security.JwtAuthentication;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
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
    public PagedResources<Resource<Post>> contents(@RequestParam(required = false) SearchRequest request,
                                                   Pageable pageable) {
        Page<Post> postList =
                (request == null)
                ? postService.findAll(pageable)
                : postService.findAll(PostSpecs.searchWith(request.getSearchKeyMap()), pageable);
        return assembler.toResource(postList);
    }

}
