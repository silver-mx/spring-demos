package dns.demo.jwt.controller;

import dns.demo.jwt.model.LoginRequest;
import dns.demo.jwt.service.TokenService;
import dns.demo.jwt.service.UserRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;


@RestController
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserRegistrationService userRegistrationService;

    public AuthController(TokenService tokenService, AuthenticationManager authenticationManager, UserRegistrationService userRegistrationService) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping("/token")
    public TokenDto token(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
        log.debug("Token requested for user[{}] with authorities[{}]",
                authentication.getName(),
                authentication.getAuthorities());
        TokenDto token = tokenService.generateToken(authentication);

        log.debug("Token[{}]", token);

        return token;
    }

    @PostMapping("/register-user")
    public ResponseEntity<Void> registerUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        boolean created = userRegistrationService.registerUser(userRegistrationDto);

        if (created) {
            return ResponseEntity.status(CREATED).build();
        }

        return ResponseEntity.status(CONFLICT).build();
    }

}
