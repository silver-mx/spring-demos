package dns.demo.jwt.service;

import dns.demo.jwt.controller.UserRegistrationDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRegistrationService {

    private final InMemoryUserDetailsManager userDetailsManager;

    public UserRegistrationService(InMemoryUserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    public boolean registerUser(UserRegistrationDto dto) {
        List<SimpleGrantedAuthority> grantedAuthorities = dto.roles().stream().map(SimpleGrantedAuthority::new).toList();
        UserDetails userDetails = new User(dto.username(),
                "{noop}" + dto.password(), grantedAuthorities);
        if (!userDetailsManager.userExists(dto.username())) {
            userDetailsManager.createUser(userDetails);
            return true;
        }

        return false;
    }
}
