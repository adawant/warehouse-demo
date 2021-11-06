package it.adawant.demo.warehouse.dto;


import lombok.*;

import java.util.List;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDto {
    private List<String> productsId;
    private String buyerEmail;
}
