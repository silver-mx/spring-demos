package dns.demo.gateway.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ResponseStatus(UNAUTHORIZED)
public class JwtInvalidException extends RuntimeException {
}
