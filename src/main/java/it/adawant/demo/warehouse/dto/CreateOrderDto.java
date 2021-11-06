package it.adawant.demo.warehouse.dto;


import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDto {
    @NotEmpty
    @Valid
    private List<@NotNull Long> productsId;
    @NotEmpty
    private String buyerEmail;
    private Instant timestamp;
}
