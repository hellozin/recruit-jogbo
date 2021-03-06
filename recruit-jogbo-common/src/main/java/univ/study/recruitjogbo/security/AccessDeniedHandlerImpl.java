package univ.study.recruitjogbo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import univ.study.recruitjogbo.api.response.ApiError;
import univ.study.recruitjogbo.api.response.ApiResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setHeader("content-type", "application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.getWriter().write(objectMapper.writeValueAsString(
                ApiResponse.ERROR(new ApiError(accessDeniedException.getMessage(), HttpStatus.FORBIDDEN))));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
