package dns.demo.jpa.dto;

import java.io.Serializable;

/**
 * DTO for {@link dns.demo.jpa.entities.Tag}
 */
public record TagDto(String tag) implements Serializable {
}