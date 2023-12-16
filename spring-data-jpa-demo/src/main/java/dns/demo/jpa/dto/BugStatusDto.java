package dns.demo.jpa.dto;

import java.io.Serializable;

/**
 * DTO for {@link dns.demo.jpa.entities.BugStatus}
 */
public record BugStatusDto(String status) implements Serializable {
}