package dns.demo.jpa.dto;

import java.io.Serializable;

/**
 * DTO for {@link dns.demo.jpa.entities.Comment}
 */
public record CommentDto(Long id, BugDto bug, AccountDto author) implements Serializable {
}