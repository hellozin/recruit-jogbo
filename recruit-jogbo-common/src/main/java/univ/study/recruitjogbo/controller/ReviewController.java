package univ.study.recruitjogbo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import univ.study.recruitjogbo.api.request.ReviewPublishRequest;
import univ.study.recruitjogbo.api.request.SearchRequest;
import univ.study.recruitjogbo.api.response.ApiResponse;
import univ.study.recruitjogbo.review.Review;
import univ.study.recruitjogbo.review.ReviewService;
import univ.study.recruitjogbo.review.ReviewSpecs;
import univ.study.recruitjogbo.security.JwtAuthentication;
import univ.study.recruitjogbo.util.EnumMapper;
import univ.study.recruitjogbo.util.EnumValue;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    private final EnumMapper enumMapper;

    private final PagedResourcesAssembler<Review> assembler;

    @GetMapping("/recruit-types")
    public List<EnumValue> recruitTypes() {
        return enumMapper.getEnumValues("recruitTypes");
    }

    @PostMapping("/review")
    public ApiResponse createPost(@AuthenticationPrincipal JwtAuthentication author,
                                  @RequestBody @Valid ReviewPublishRequest reviewPublishRequest) {
        Review review = reviewService.publish(author.id, reviewPublishRequest);
        return ApiResponse.OK(review);
    }

    @GetMapping("/review/list")
    public ApiResponse showPostList(SearchRequest request,
                                    @PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Review> postList = request.getSearchKeyMap().isEmpty()
                ? reviewService.findAll(pageable)
                : reviewService.findAll(ReviewSpecs.searchWith(request.getSearchKeyMap()), pageable);
        return ApiResponse.OK(assembler.toResource(postList));
    }

    @GetMapping("/review/{id}")
    public ApiResponse getPost(@PathVariable(value = "id") Review review) {
        return ApiResponse.OK(review);
    }

    @PutMapping("/review/{id}")
    public ApiResponse editPost(@PathVariable(value = "id") Long reviewId, @RequestBody @Valid ReviewPublishRequest reviewPublishRequest) {
        Review review = reviewService.edit(reviewId, reviewPublishRequest);
        return ApiResponse.OK(review);
    }

    @DeleteMapping("/review/{id}")
    public ApiResponse deletePost(@PathVariable(value = "id") Long reviewId) {
        reviewService.delete(reviewId);
        return ApiResponse.OK(reviewId);
    }

}
