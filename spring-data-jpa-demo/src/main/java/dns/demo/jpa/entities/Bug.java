package dns.demo.jpa.entities;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NamedEntityGraphs({
        @NamedEntityGraph(name = "graph.bug.with-tags", attributeNodes = {
                @NamedAttributeNode("reportedBy"),
                @NamedAttributeNode("assignedTo"),
                @NamedAttributeNode("verifiedBy"),
                @NamedAttributeNode("status"),
                @NamedAttributeNode("tags")
        }),
        @NamedEntityGraph(name = "graph.bug.with-screenshots", attributeNodes = {
                @NamedAttributeNode("screenshots")
        })
})
@Entity
@Table(name = "bugs")
public class Bug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bug_id", nullable = false)
    private Long id;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "date_reported", nullable = false)
    private LocalDate dateReported;

    @Column(name = "summary", length = 80)
    private String summary;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "resolution", length = 1000)
    private String resolution;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "reported_by", nullable = false)
    private Account reportedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "assigned_to")
    private Account assignedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "verified_by")
    private Account verifiedBy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "status", nullable = false)
    private BugStatus status;

    @Column(name = "priority", length = 20)
    private String priority;

    @Column(name = "hours", precision = 9)
    private BigDecimal hours;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bug", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private List<Tag> tags;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bug", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private List<Screenshot> screenshots;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Bug bug = (Bug) o;
        return getId() != null && Objects.equals(getId(), bug.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}