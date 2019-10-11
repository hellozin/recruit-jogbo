package univ.study.recruitjogbo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import univ.study.recruitjogbo.api.request.PostingRequest;
import univ.study.recruitjogbo.api.request.SearchRequest;
import univ.study.recruitjogbo.api.response.ApiResponse;
import univ.study.recruitjogbo.post.Post;
import univ.study.recruitjogbo.post.PostService;
import univ.study.recruitjogbo.post.PostSpecs;
import univ.study.recruitjogbo.post.RecruitTypes;
import univ.study.recruitjogbo.security.JwtAuthentication;

import java.util.Arrays;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    private final PagedResourcesAssembler<Post> assembler;

    @ModelAttribute("recruitTypes")
    public Collection<RecruitTypes> recruitTypes() {
        return Arrays.asList(RecruitTypes.values());
    }

    @PostMapping("/post")
    public ApiResponse createPost(@AuthenticationPrincipal JwtAuthentication author,
                                  @RequestBody PostingRequest postingRequest) {
        Post post = postService.write(author.id, postingRequest);
        return ApiResponse.OK(post);
    }

    @GetMapping("/post/list")
    public ApiResponse showPostList(SearchRequest request,
                                    @PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Post> postList = request.getSearchKeyMap().isEmpty()
                ? postService.findAll(pageable)
                : postService.findAll(PostSpecs.searchWith(request.getSearchKeyMap()), pageable);
        return ApiResponse.OK(assembler.toResource(postList));
    }

    @GetMapping("/post/{id}")
    public ApiResponse getPost(@PathVariable(value = "id") Post post) {
        return ApiResponse.OK(post);
    }

    @PutMapping("/post/{id}")
    public ApiResponse editPost(@PathVariable(value = "id") Long postId, @RequestBody PostingRequest postingRequest) {
        Post post = postService.edit(postId, postingRequest);
        return ApiResponse.OK(post);
    }

    @DeleteMapping("/post/{id}")
    public ApiResponse deletePost(@PathVariable(value = "id") Long postId) {
        postService.delete(postId);
        return ApiResponse.OK(postId);
    }

}
