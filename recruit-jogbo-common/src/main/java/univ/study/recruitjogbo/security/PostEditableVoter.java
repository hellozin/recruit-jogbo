package univ.study.recruitjogbo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.RequestMatcher;
import univ.study.recruitjogbo.error.NotFoundException;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.review.Review;
import univ.study.recruitjogbo.review.ReviewService;
import univ.study.recruitjogbo.tip.Tip;
import univ.study.recruitjogbo.tip.TipService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.apache.commons.lang3.math.NumberUtils.toLong;

@RequiredArgsConstructor
public class PostEditableVoter implements AccessDecisionVoter<FilterInvocation> {

    private final RequestMatcher requestMatcher;

    private final Function<String, Map<String, String>> extractor;

    private static final List<String> ignoreMethods = Arrays.asList(HttpMethod.GET.name(), HttpMethod.OPTIONS.name());

    private ReviewService reviewService;

    private TipService tipService;

    // TODO: 2019-11-03 사용하지 않는 의존성 해결하기.
    /*
    * 하나만 사용하는 의존성을 위해 reviewService, tipService 모두 주입받아야 함.
    * Class 를 분리하면 중복 코드가 많이생김.
    * */

    @Override
    public int vote(Authentication authentication, FilterInvocation object, Collection<ConfigAttribute> attributes) {
        HttpServletRequest request = object.getRequest();

        if (!requestMatcher.matches(request)) {
            return ACCESS_ABSTAIN;
        }

        if (ignoreMethods.contains(request.getMethod())) {
            return ACCESS_GRANTED;
        }

        if (!authentication.getClass().isAssignableFrom(JwtAuthenticationToken.class)) {
            return ACCESS_ABSTAIN;
        }

        Map<String, String> apply = extractor.apply(request.getRequestURI());
        String requestDomain = apply.get("domain");
        Long requestId = toLong(apply.get("id"), -1);

        Long authorId = -1L;
        switch (requestDomain) {
            case "review":
                authorId = reviewService.findById(requestId)
                        .map(Review::getAuthor)
                        .map(Member::getId)
                        .orElseThrow(() -> new NotFoundException(Review.class, requestId.toString()));
                break;
            case "tip":
                authorId = tipService.findById(requestId)
                        .map(Tip::getAuthor)
                        .map(Member::getId)
                        .orElseThrow(() -> new NotFoundException(Tip.class, requestId.toString()));
                break;
        }

        JwtAuthentication currentMember = (JwtAuthentication) authentication.getPrincipal();
        if (currentMember.id.equals(authorId)) {
            return ACCESS_GRANTED;
        }

        return ACCESS_DENIED;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(FilterInvocation.class);
    }

    @Autowired
    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Autowired
    public void setTipService(TipService tipService) {
        this.tipService = tipService;
    }

}
