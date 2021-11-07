package it.adawant.demo.warehouse.command.product;


import it.adawant.demo.warehouse.dto.UpdateProductDto;
import it.adawant.demo.warehouse.mapper.ProductMapper;
import it.adawant.demo.warehouse.model.ProductModel;
import it.adawant.demo.warehouse.service.WarehouseService;
import it.adawant.demo.warehouse.utils.BaseCommand;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class UpdateProductCommand implements BaseCommand<ProductModel> {

    private final Long id;
    private final UpdateProductDto updateProductDto;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private WarehouseService warehouseService;

    @Override
    public ProductModel execute() {
        val product = productMapper.updateDtoToModel(updateProductDto);
        product.setId(id);
        return warehouseService.updateProduct(product);
    }
}
