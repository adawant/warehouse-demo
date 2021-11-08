package it.adawant.demo.warehouse.utils;

import it.adawant.demo.warehouse.dto.CreateOrderDto;
import it.adawant.demo.warehouse.dto.CreateProductDto;
import it.adawant.demo.warehouse.dto.UpdateProductDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class DtoProvider {
    private DtoProvider() {
    }

    public static CreateProductDto createProductDtoA() {
        return new CreateProductDto("A", BigDecimal.valueOf(1.33));
    }

    public static CreateProductDto createProductDtoB() {
        return new CreateProductDto("B", BigDecimal.valueOf(2.92));

    }

    public static CreateProductDto createProductDtoC() {
        return new CreateProductDto("C", BigDecimal.valueOf(10.27));
    }


    public static CreateOrderDto createOrderDto(List<Long> ids, Instant instant) {
        return new CreateOrderDto(ids, "me@my.test", instant);
    }


}
