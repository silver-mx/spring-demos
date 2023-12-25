package dns.demo.jwt.controller;

public record TokenDto(String token, long expiresAt) {
}