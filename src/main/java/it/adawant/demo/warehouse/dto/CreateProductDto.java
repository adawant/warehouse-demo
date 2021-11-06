package it.adawant.demo.warehouse.dto;


import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductDto {
    @NotEmpty
    private String name;
    @NotNull
    @Min(0)
    private BigDecimal price;
}
