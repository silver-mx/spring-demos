package dns.demo.jpa.dto;

import java.io.Serializable;

/**
 * DTO for {@link dns.demo.jpa.entities.TagId}
 */
public record TagIdDto(String tag) implements Serializable {
}