package dns.demo.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Embeddable
public class BugProductId implements Serializable {
    private static final long serialVersionUID = -5226032829084947641L;
    @Column(name = "bug_id", nullable = false)
    private Long bugId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BugProductId entity = (BugProductId) o;
        return Objects.equals(this.bugId, entity.bugId) &&
                Objects.equals(this.productId, entity.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bugId, productId);
    }

}