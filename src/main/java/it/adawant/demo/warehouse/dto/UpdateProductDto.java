package it.adawant.demo.warehouse.dto;


import lombok.*;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductDto {
    private String name;
    @Min(0)
    private BigDecimal price;
}
