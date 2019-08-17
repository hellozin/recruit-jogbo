package univ.study.recruitjogbo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import univ.study.recruitjogbo.member.RecruitType;
import univ.study.recruitjogbo.post.Post;
import univ.study.recruitjogbo.post.PostService;
import univ.study.recruitjogbo.post.PostSpecs;
import univ.study.recruitjogbo.post.SearchRequest;

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

    @GetMapping("/post/list")
    public String showPostList(Map<String, Object> model,
                               @RequestParam(required = false) SearchRequest request,
                               Pageable pageable) {
        Page<Post> postList = request == null
                ? postService.findAll(pageable)
                : postService.findAll(PostSpecs.searchWith(request), pageable);
        model.put("postList", postList);
        return "postList";
    }

    @GetMapping("/post/new")
    public String showPostCreateForm(Post post) {
        return "newPostForm";
    }

    @PostMapping("/post")
    public String createPost(Post post, Map<String, Object> model) {
        postService.write(
                1L,
                post.getCompanyName(),
                post.getRecruitType(),
                post.getDeadLine(),
                post.getReview()
        );
        return "redirect:/post/list";
    }

}
