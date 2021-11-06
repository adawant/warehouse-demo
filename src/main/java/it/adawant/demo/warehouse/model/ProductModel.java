package it.adawant.demo.warehouse.model;


import lombok.*;

import java.math.BigDecimal;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {
    private String id;
    private String name;
    private BigDecimal price;
}
