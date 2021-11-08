package it.adawant.demo.warehouse.command.order;

import it.adawant.demo.warehouse.model.OrderModel;
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
public class GetOrderByIdCommand implements BaseCommand<OrderModel> {

    private final Long id;
    @Autowired
    private WarehouseService warehouseService;

    @Override
    public OrderModel execute() {
        return warehouseService.getOrderById(id);
    }
}
