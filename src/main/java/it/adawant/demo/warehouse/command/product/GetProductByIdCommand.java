package it.adawant.demo.warehouse.command.product;

import it.adawant.demo.warehouse.model.ProductModel;
import it.adawant.demo.warehouse.service.WarehouseService;
import it.adawant.demo.warehouse.utils.BaseCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class GetProductByIdCommand implements BaseCommand<ProductModel> {

    private final Long id;
    @Autowired
    private WarehouseService warehouseService;

    @Override
    public ProductModel execute() {
        return warehouseService.getProductById(id);
    }
}
