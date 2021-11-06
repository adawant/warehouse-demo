package it.adawant.demo.warehouse.command.product;

import it.adawant.demo.warehouse.command.BaseCommand;
import it.adawant.demo.warehouse.model.ProductModel;
import it.adawant.demo.warehouse.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class GetProductByIdCommand implements BaseCommand<ProductModel> {

    private final WarehouseService warehouseService;

    @Override
    public ProductModel execute() {
        return null;
    }
}