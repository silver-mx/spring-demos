package dns.demo.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Embeddable
public class ScreenshotId implements Serializable {
    private static final long serialVersionUID = 5393369304287700956L;
    @Column(name = "bug_id", nullable = false)
    private Long bugId;

    @Column(name = "image_id", nullable = false)
    private Long imageId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ScreenshotId entity = (ScreenshotId) o;
        return Objects.equals(this.imageId, entity.imageId) &&
                Objects.equals(this.bugId, entity.bugId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageId, bugId);
    }

}