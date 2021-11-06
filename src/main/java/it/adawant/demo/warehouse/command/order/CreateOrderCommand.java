package it.adawant.demo.warehouse.command.order;

import it.adawant.demo.warehouse.command.BaseCommand;
import it.adawant.demo.warehouse.dto.CreateOrderDto;
import it.adawant.demo.warehouse.model.OrderModel;
import it.adawant.demo.warehouse.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CreateOrderCommand implements BaseCommand<OrderModel> {

    private final WarehouseService warehouseService;
    private final CreateOrderDto createOrderDto;

    @Override
    public OrderModel execute() {
        return null;
    }
}
