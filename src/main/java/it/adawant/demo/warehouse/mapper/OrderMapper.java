package it.adawant.demo.warehouse.mapper;

import it.adawant.demo.warehouse.model.OrderModel;
import it.adawant.demo.warehouse.model.ProductModel;
import it.adawant.demo.warehouse.resource.OrderResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {

    @Mapping(target = "productsId", source = "products")
    public abstract OrderResource modelToResource(OrderModel orderModel);

    protected String mapProductsToString(ProductModel productModel) {
        return null;
    }

}
