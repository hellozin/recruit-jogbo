package univ.study.recruitjogbo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import univ.study.recruitjogbo.post.Post;
import univ.study.recruitjogbo.post.PostService;
import univ.study.recruitjogbo.post.PostSpecs;
import univ.study.recruitjogbo.post.RecruitTypes;
import univ.study.recruitjogbo.request.PostingRequest;
import univ.study.recruitjogbo.request.SearchRequest;
import univ.study.recruitjogbo.security.AuthMember;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @ModelAttribute("recruitTypes")
    public Collection<RecruitTypes> recruitTypes() {
        return Arrays.asList(RecruitTypes.values());
    }

    @ModelAttribute("searchKeys")
    public Collection<PostSpecs.SearchKeys> searchKeys() {
        return Arrays.asList(PostSpecs.SearchKeys.values());
    }

    @GetMapping("/post")
    public String showPostCreateForm(PostingRequest postingRequest) {
        return "post/newPostForm";
    }

    @PostMapping("/post")
    public String createPost(@AuthenticationPrincipal AuthMember author, PostingRequest postingRequest) {
        postService.write(author.getId(), postingRequest);
        return "redirect:/post/list";
    }

    @GetMapping("/post/list")
    public String showPostList(SearchRequest request,
                               Map<String, Object> model,
                               @PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC)
                               Pageable pageable) {
        Page<Post> postList = request.getSearchKeyMap().isEmpty()
                ? postService.findAll(pageable)
                : postService.findAll(PostSpecs.searchWith(request.getSearchKeyMap()), pageable);
        model.put("postList", postList);

        int totalPages = postList.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.put("pageNumbers", pageNumbers);
        }

        if (postList.hasPrevious()) {
            int prevPageNumber = postList.previousPageable().getPageNumber();
            model.put("prevPageNumber", prevPageNumber);
        }

        if (postList.hasNext()) {
            int nextPageNumber = postList.nextPageable().getPageNumber();
            model.put("nextPageNumber", nextPageNumber);
        }
        return "post/postList";
    }

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable(value = "id") Post post,
                          @AuthenticationPrincipal AuthMember author,
                          Map<String, Object> model) {
//        model.put("post", post);
        model.put("isAuthor", post.getAuthor().getId().equals(author.getId()));
        return "post/post";
    }

    @GetMapping("/post/{id}/edit")
    public String showEditPostForm(@PathVariable(value = "id") Post post,
                                   PostingRequest postingRequest,
                                   Map<String, Object> model) {
//        model.put("post", post);
        return "post/editPostForm";
    }

    @PutMapping("/post/{id}")
    public String editPost(@PathVariable(value = "id") Long postId, PostingRequest postingRequest) {
        postService.edit(postId, postingRequest);
        return "redirect:/post/"+postId;
    }

}
