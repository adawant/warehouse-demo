package it.adawant.demo.warehouse.mapper;

import it.adawant.demo.warehouse.dto.CreateProductDto;
import it.adawant.demo.warehouse.dto.UpdateProductDto;
import it.adawant.demo.warehouse.entity.ProductEntity;
import it.adawant.demo.warehouse.model.ProductModel;
import it.adawant.demo.warehouse.resource.ProductResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "price", expression = "java(createProductDto.getPrice().stripTrailingZeros())")
    ProductModel createDtoToModel(CreateProductDto createProductDto);

    @Mapping(target = "price", expression = "java( updateProductDto.getPrice() != null? " +
            "updateProductDto.getPrice().stripTrailingZeros() : null)")
    ProductModel updateDtoToModel(UpdateProductDto updateProductDto);

    @Mapping(target = "price", expression = "java(productModel.getPrice().stripTrailingZeros())")
    ProductEntity modelToEntity(ProductModel productModel);

    @Mapping(target = "price", expression = "java(productEntity.getPrice().stripTrailingZeros())")
    ProductModel entityToModel(ProductEntity productEntity);

    List<ProductModel> entitiesToModel(Iterable<ProductEntity> productEntity);


    ProductResource modelToResource(ProductModel productModel);

}
