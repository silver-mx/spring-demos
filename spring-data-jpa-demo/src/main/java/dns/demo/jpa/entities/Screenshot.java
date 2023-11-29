package dns.demo.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "screenshots")
public class Screenshot {
    @EmbeddedId
    private ScreenshotId id;

    @JsonIgnore
    @MapsId("bugId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "bug_id", nullable = false)
    private Bug bug;

    @Column(name = "caption", length = 100)
    private String caption;

/*
    TODO [JPA Buddy] create field to map the 'screenshot_image' column
     Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "screenshot_image", columnDefinition = "binary large object(2147483647, 0)")
    private Object screenshotImage;
*/
}