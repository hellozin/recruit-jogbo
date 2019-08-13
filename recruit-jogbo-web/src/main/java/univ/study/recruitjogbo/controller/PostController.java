package univ.study.recruitjogbo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import univ.study.recruitjogbo.post.Post;
import univ.study.recruitjogbo.post.PostService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/post/list")
    public String showPostList(Map<String, Object> model, Pageable pageable) {
        model.put("postList", postService.findAll(pageable));
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
