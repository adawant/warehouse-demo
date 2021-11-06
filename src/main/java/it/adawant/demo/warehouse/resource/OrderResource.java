package it.adawant.demo.warehouse.resource;


import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResource {
    private Long id;
    private List<Long> productsId;
    private String buyerEmail;
    private Instant timestamp;
    private BigDecimal total;
}
