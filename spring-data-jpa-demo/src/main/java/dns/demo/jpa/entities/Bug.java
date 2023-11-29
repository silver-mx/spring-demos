package dns.demo.jpa.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NamedEntityGraph(name = "graph.bug.complete", attributeNodes = {
        @NamedAttributeNode("reportedBy"),
        @NamedAttributeNode("assignedTo"),
        @NamedAttributeNode("verifiedBy"),
        @NamedAttributeNode("tags")/*,
        @NamedAttributeNode("screenshots")*/
})
@Entity
@Table(name = "bugs")
public class Bug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bug_id", nullable = false)
    private Long id;

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
    @Fetch(FetchMode.SUBSELECT)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private List<Tag> tags;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bug", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private List<Screenshot> screenshots;

}