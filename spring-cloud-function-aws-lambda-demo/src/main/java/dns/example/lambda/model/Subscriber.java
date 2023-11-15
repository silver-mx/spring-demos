package dns.example.lambda.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Subscriber {
    private Integer id;
    private String email;
}
