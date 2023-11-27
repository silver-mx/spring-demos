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
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "product_id", nullable = false)
    private Long id;

    @Column(name = "product_name", length = 50)
    private String productName;

}