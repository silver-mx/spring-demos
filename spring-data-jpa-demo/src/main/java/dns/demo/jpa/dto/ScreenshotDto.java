package dns.demo.jpa.dto;

import java.io.Serializable;

/**
 * DTO for {@link dns.demo.jpa.entities.Screenshot}
 */
public record ScreenshotDto(ScreenshotIdDto id, String caption) implements Serializable {
}