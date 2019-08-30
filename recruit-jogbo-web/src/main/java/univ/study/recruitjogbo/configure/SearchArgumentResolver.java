package univ.study.recruitjogbo.configure;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import univ.study.recruitjogbo.post.PostSpecs;
import univ.study.recruitjogbo.request.SearchRequest;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SearchArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(SearchRequest.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Map<PostSpecs.SearchKeys, Object> searchKeysMap = new HashMap<>();
        for (PostSpecs.SearchKeys key : PostSpecs.SearchKeys.values()) {
            String param = webRequest.getParameter(key.getValue());
            if (isNotBlank(param)) {
                searchKeysMap.put(key, param);
            }
        }
        return new SearchRequest(searchKeysMap);
    }
}
