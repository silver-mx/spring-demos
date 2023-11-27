package dns.demo.jpa.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "bug_id", nullable = false)
    private Bug bug;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "author", nullable = false)
    private Account author;

/*
    TODO [JPA Buddy] create field to map the 'comment_date' column
     Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "comment_date", columnDefinition = "timestamp(6) not null")
    private Object commentDate;
*/
/*
    TODO [JPA Buddy] create field to map the 'comment' column
     Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "comment", columnDefinition = "character large object(2147483647, 0) not null")
    private Object comment;
*/
}