package dns.demo.jwt.service;

import dns.demo.jwt.controller.TokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private final Duration jwtExpiration;
    private final JwtEncoder jwtEncoder;

    public TokenService(@Value("${application.jwt.expiration}") Duration jwtExpiration,
                        JwtEncoder jwtEncoder) {
        this.jwtExpiration = jwtExpiration;
        this.jwtEncoder = jwtEncoder;
    }

    public TokenDto generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("diego")
                .issuedAt(now)
                .expiresAt(now.plus(jwtExpiration))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        String tokenValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new TokenDto(tokenValue, claims.getExpiresAt().getEpochSecond());
    }
}
