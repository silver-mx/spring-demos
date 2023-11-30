package dns.demo.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Embeddable
public class TagId implements Serializable {
    private static final long serialVersionUID = -8433026336408775576L;
    @Column(name = "bug_id", nullable = false)
    private Long bugId;

    @Column(name = "tag", nullable = false, length = 20)
    private String tag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TagId entity = (TagId) o;
        return Objects.equals(this.bugId, entity.bugId) &&
                Objects.equals(this.tag, entity.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bugId, tag);
    }

}