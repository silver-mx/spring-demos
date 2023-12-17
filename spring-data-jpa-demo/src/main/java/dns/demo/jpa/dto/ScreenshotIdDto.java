package dns.demo.jpa.dto;

import java.io.Serializable;

/**
 * DTO for {@link dns.demo.jpa.entities.ScreenshotId}
 */
public record ScreenshotIdDto(Long bugId, Long imageId) implements Serializable {
}