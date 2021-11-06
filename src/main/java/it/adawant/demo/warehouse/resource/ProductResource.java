package it.adawant.demo.warehouse.resource;


import lombok.*;

import java.math.BigDecimal;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResource {
    private Long id;
    private String name;
    private BigDecimal price;
}
