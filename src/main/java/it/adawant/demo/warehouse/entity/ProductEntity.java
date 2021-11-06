package it.adawant.demo.warehouse.entity;


import it.adawant.demo.warehouse.utils.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity extends BaseEntity<Long> {
    private String name;
    private BigDecimal price;
    @ManyToMany(mappedBy = "products")
    private List<OrderEntity> orders;
}
