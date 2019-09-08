package univ.study.recruitjogbo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import univ.study.recruitjogbo.member.RecruitType;
import univ.study.recruitjogbo.post.Post;
import univ.study.recruitjogbo.post.PostService;
import univ.study.recruitjogbo.post.PostSpecs;
import univ.study.recruitjogbo.request.PostingRequest;
import univ.study.recruitjogbo.request.SearchRequest;
import univ.study.recruitjogbo.security.AuthMember;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @ModelAttribute("recruitTypes")
    public Collection<RecruitType> recruitTypes() {
        return Arrays.asList(RecruitType.values());
    }

    @ModelAttribute("searchKeys")
    public Collection<PostSpecs.SearchKeys> searchKeys() {
        return Arrays.asList(PostSpecs.SearchKeys.values());
    }

    @GetMapping("/post")
    public String showPostCreateForm(Post post) {
        return "newPostForm";
    }

    @PostMapping("/post")
    public String createPost(@AuthenticationPrincipal AuthMember author, Post post, Map<String, Object> model) {
        postService.write(
                author.getId(),
                post.getCompanyName(),
                post.getRecruitType(),
                post.getDeadLine(),
                post.getReview()
        );
        return "redirect:/post/list";
    }

    @GetMapping("/post/list")
    public String showPostList(SearchRequest request,
                               Map<String, Object> model,
                               Pageable pageable) {
        Page<Post> postList = request == null
                ? postService.findAll(pageable)
                : postService.findAll(PostSpecs.searchWith(request.getSearchKeyMap()), pageable);
        model.put("postList", postList);
        return "postList";
    }

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable(value = "id") Post post,
                          @AuthenticationPrincipal AuthMember author,
                          Map<String, Object> model) {
        model.put("post", post);
        model.put("isAuthor", post.getAuthor().getId().equals(author.getId()));
        return "post";
    }

    @GetMapping("/post/{id}/edit")
    public String showEditPostForm(@PathVariable(value = "id") Post post, Map<String, Object> model) {
        model.put("post", post);
        model.put("postingRequest", new PostingRequest());
        return "editPostForm";
    }

    @PutMapping("/post/{id}")
    public String editPost(PostingRequest request, @PathVariable(value = "id") Long postId) {
        postService.edit(
                postId,
                request.getCompanyName(),
                request.getRecruitType(),
                request.getDeadLine(),
                request.getReview());
        return "redirect:/post/"+postId;
    }

}
