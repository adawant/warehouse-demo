package it.adawant.demo.warehouse.mapper;

import it.adawant.demo.warehouse.dto.CreateProductDto;
import it.adawant.demo.warehouse.dto.UpdateProductDto;
import it.adawant.demo.warehouse.entity.ProductEntity;
import it.adawant.demo.warehouse.model.ProductModel;
import it.adawant.demo.warehouse.resource.ProductResource;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductModel createDtoToModel(CreateProductDto createProductDto);

    ProductModel updateDtoToModel(UpdateProductDto updateProductDto);

    ProductEntity modelToEntity(ProductModel productModel);

    ProductModel entityToModel(ProductEntity productEntity);

    List<ProductModel> entitiesToModel(Iterable<ProductEntity> productEntity);


    ProductResource modelToResource(ProductModel productModel);

}
