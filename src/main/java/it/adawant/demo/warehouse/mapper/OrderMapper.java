package it.adawant.demo.warehouse.mapper;

import it.adawant.demo.warehouse.entity.OrderEntity;
import it.adawant.demo.warehouse.model.OrderModel;
import it.adawant.demo.warehouse.model.ProductModel;
import it.adawant.demo.warehouse.resource.OrderResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {

    @Mapping(target = "productsId", source = "products")
    @Mapping(target = "total", expression = "java(orderModel.getTotal().stripTrailingZeros())")
    public abstract OrderResource modelToResource(OrderModel orderModel);

    @Mapping(target = "total", expression = "java(orderModel.getTotal().stripTrailingZeros())")
    public abstract OrderEntity modelToEntity(OrderModel orderModel);

    @Mapping(target = "total", expression = "java(orderEntity.getTotal().stripTrailingZeros())")
    public abstract OrderModel entityToModel(OrderEntity orderEntity);

    protected Long mapProductsToLong(ProductModel productModel) {
        return productModel.getId();
    }

}
