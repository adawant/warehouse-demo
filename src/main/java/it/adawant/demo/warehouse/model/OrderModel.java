package it.adawant.demo.warehouse.model;


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
public class OrderModel {
    private Long id;
    private List<ProductModel> products;
    private String buyerEmail;
    private Instant timestamp;
    private BigDecimal total;
}
