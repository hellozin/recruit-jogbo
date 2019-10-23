package univ.study.recruitjogbo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.RequestMatcher;
import univ.study.recruitjogbo.api.response.AuthenticationResult;
import univ.study.recruitjogbo.error.NotFoundException;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.post.Post;
import univ.study.recruitjogbo.post.PostService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class PostEditableVoter implements AccessDecisionVoter<FilterInvocation> {

    private final RequestMatcher requestMatcher;

    private final Function<String, Long> idExtractor;

    private static final List<String> ignoreMethods = Arrays.asList(HttpMethod.GET.name(), HttpMethod.OPTIONS.name());

    private PostService postService;

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

        Long requestPostId = idExtractor.apply(request.getRequestURI());
        Long authorId = postService.findById(requestPostId)
                .map(Post::getAuthor)
                .map(Member::getId)
                .orElseThrow(() -> new NotFoundException(Post.class, requestPostId.toString()));

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
    public void setPostService(PostService postService) {
        this.postService = postService;
    }

}
