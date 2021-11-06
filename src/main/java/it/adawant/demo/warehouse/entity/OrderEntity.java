package it.adawant.demo.warehouse.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

//TODO
public class OrderEntity {
    private String id;
    private List<ProductEntity> products;
    private String buyerEmail;
    private Instant timestamp;
    private BigDecimal total;
}
