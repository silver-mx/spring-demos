package dns.demo.jpa.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link dns.demo.jpa.entities.Account}
 */
public record AccountDto(Long id, String accountName, String firstName, String lastName, String email,
                         String passwordHash, BigDecimal hourlyRate) implements Serializable {
}