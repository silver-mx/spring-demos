package dns.demo.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "bugstatus")
public class BugStatus {
    @Id
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    //TODO [JPA Buddy] generate columns from DB
}