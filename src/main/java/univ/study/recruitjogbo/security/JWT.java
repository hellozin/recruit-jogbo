package univ.study.recruitjogbo.security;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

import static com.auth0.jwt.JWT.require;

@Getter
public final class JWT {

    private final String issuer;

    private final String clientSecret;

    private final int expirySeconds;

    private final Algorithm algorithm;

    private final JWTVerifier jwtVerifier;

    public JWT(String issuer, String clientSecret, int expirySeconds) {
        this.issuer = issuer;
        this.clientSecret = clientSecret;
        this.expirySeconds = expirySeconds;
        this.algorithm = Algorithm.HMAC512(clientSecret);
        this.jwtVerifier = require(algorithm)
                .withIssuer(issuer)
                .build();
    }

    public String newToken(Claims claims) {
        Date now = new Date();
        JWTCreator.Builder builder = com.auth0.jwt.JWT.create();
        builder.withIssuer(issuer);
        builder.withIssuedAt(now);
        if (expirySeconds > 0) {
            builder.withExpiresAt(new Date(now.getTime() + expirySeconds * 1_000L));
        }
        builder.withClaim("memberKey", claims.memberKey);
        builder.withClaim("memberId", claims.memberId);
        builder.withClaim("name", claims.name);
        builder.withClaim("email", claims.email);
        builder.withArrayClaim("roles", claims.roles);
        return builder.sign(algorithm);
    }

    public String refreshToken(String token) {
        Claims claims = verify(token);
        claims.eraseIat();
        claims.eraseExp();
        return newToken(claims);
    }

    public Claims verify(String token) {
        return new Claims(jwtVerifier.verify(token));
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    static public class Claims {
        Long memberKey;
        String memberId;
        String name;
        String email;
        String[] roles;
        Date iat;
        Date exp;

        Claims(DecodedJWT decodedJWT) {
            Claim memberKey = decodedJWT.getClaim("memberKey");
            if(!memberKey.isNull())
                this.memberKey = memberKey.asLong();

            Claim memberId = decodedJWT.getClaim("memberId");
            if (!memberId.isNull())
                this.memberId = memberId.asString();

            Claim name = decodedJWT.getClaim("name");
            if (!name.isNull())
                this.name = name.asString();

            Claim email = decodedJWT.getClaim("email");
            if (!email.isNull())
                this.email = email.asString();

            Claim roles = decodedJWT.getClaim("roles");
            if (!roles.isNull())
                this.roles = roles.asArray(String.class);

            this.iat = decodedJWT.getIssuedAt();
            this.exp = decodedJWT.getExpiresAt();
        }

        public static Claims of(Long memberKey, String memberId, String name, String email, String[] roles) {
            Claims claims = new Claims();
            claims.memberKey = memberKey;
            claims.memberId = memberId;
            claims.name = name;
            claims.email = email;
            claims.roles = roles;
            return claims;
        }

        Long iat() {
            return iat != null ? iat.getTime() : -1;
        }

        Long exp() {
            return exp != null ? exp.getTime() : -1;
        }

        void eraseIat() {
            iat = null;
        }

        void eraseExp() {
            exp = null;
        }

    }

}
