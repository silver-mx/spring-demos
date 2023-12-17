package dns.demo.jpa.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for {@link dns.demo.jpa.entities.Bug}
 */

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BugDto implements Serializable {
    Long id;
    Integer version;
    LocalDate dateReported;
    String summary;
    String description;
    String resolution;
    AccountDto reportedBy;
    AccountDto assignedTo;
    AccountDto verifiedBy;
    BugStatusDto status;
    String priority;
    BigDecimal hours;
    List<TagDto> tags;
    List<ScreenshotDto> screenshots;
}