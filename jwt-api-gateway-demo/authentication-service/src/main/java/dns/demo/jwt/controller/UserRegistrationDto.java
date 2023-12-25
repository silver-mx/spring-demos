package dns.demo.jwt.controller;

import java.util.List;

public record UserRegistrationDto(String username, String password,
                                  List<String> roles) {
}
