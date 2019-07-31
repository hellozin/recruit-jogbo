package univ.study.recruitjogbo.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ObjectUtils.allNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
public class JwtAuthenticationTokenFilter extends GenericFilterBean {

    private static final Pattern BEARER = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);

    private final long REFRESH_RANGE_MILLIS = 6000 * 10;

    @Autowired
    private JWT jwt;

    @Value("${jwt.token.header}")
    private String tokenHeader;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String authorizationToken = obtainAuthorizationToken(request);
            if (isNotBlank(authorizationToken)) {
                try {
                    JWT.Claims claims = jwt.verify(authorizationToken);
                    log.debug("JWT parse result: {}", claims);

                    if (canRefresh(claims, REFRESH_RANGE_MILLIS)) {
                        String refreshedToken = jwt.refreshToken(authorizationToken);
                        response.setHeader(tokenHeader, refreshedToken);
                    }

                    Long memberKey = claims.memberKey;
                    String memberId = claims.memberId;
                    String name = claims.name;
                    String email = claims.email;
                    List<GrantedAuthority> authorities = obtainAuthorities(claims);

                    log.info("========== before set details =========");
                    if (allNotNull(memberKey, email) && isNotBlank(name) && !authorities.isEmpty()) {
                        log.info("========== enter set details =========");
                        JwtAuthenticationToken authentication = new JwtAuthenticationToken(
                                        new JwtAuthentication(memberKey, memberId, name, email), null, authorities);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.info("========== end set details =========");
                    }
                } catch (Exception e) {
                    log.warn("JWT processing failed: {}", e.getMessage());
                }
            }
        }
        chain.doFilter(request, response);
    }

    private List<GrantedAuthority> obtainAuthorities(JWT.Claims claims) {
        String[] roles = claims.roles;
        return ArrayUtils.isEmpty(roles) ?
                Collections.emptyList() :
                Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(toList());
    }

    private String obtainAuthorizationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        if (isNotBlank(token)) {
            try {
                token = URLDecoder.decode(token, "UTF-8");
                String[] parts = token.split(" ");
                if (parts.length == 2) {
                    String scheme = parts[0];
                    String credentials = parts[1];
                    return BEARER.matcher(scheme).matches() ? credentials : null;
                }
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    private boolean canRefresh(JWT.Claims claims, long refreshRangeMillis) {
        long exp = claims.exp();
        if (exp > 0) {
            long remain = exp - System.currentTimeMillis();
            return remain < refreshRangeMillis;
        }
        return false;
    }

}
