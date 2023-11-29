package dns.demo.jpa.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", nullable = false)
    private Long id;

    @Column(name = "account_name", length = 20)
    private String accountName;

    @Column(name = "first_name", length = 20)
    private String firstName;

    @Column(name = "last_name", length = 20)
    private String lastName;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "password_hash", length = 64)
    private String passwordHash;

    @Column(name = "hourly_rate", precision = 9)
    private BigDecimal hourlyRate;

/*
    TODO [JPA Buddy] create field to map the 'portrait_image' column
     Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "portrait_image", columnDefinition = "binary large object(2147483647, 0)")
    private Object portraitImage;
*/
}