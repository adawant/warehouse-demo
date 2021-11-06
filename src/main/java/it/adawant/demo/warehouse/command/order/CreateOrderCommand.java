package it.adawant.demo.warehouse.command.order;

import it.adawant.demo.warehouse.utils.BaseCommand;
import it.adawant.demo.warehouse.dto.CreateOrderDto;
import it.adawant.demo.warehouse.model.OrderModel;
import it.adawant.demo.warehouse.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CreateOrderCommand implements BaseCommand<OrderModel> {

    private final CreateOrderDto createOrderDto;

    @Autowired
    private WarehouseService warehouseService;

    @Override
    public OrderModel execute() {
        val linkedProducts = warehouseService.getProductsById(createOrderDto.getProductsId());
        val orderModel = new OrderModel();

        orderModel.setBuyerEmail(createOrderDto.getBuyerEmail());
        orderModel.setProducts(linkedProducts);

        if (createOrderDto.getTimestamp() == null)
            orderModel.setTimestamp(Instant.now());
        else
            orderModel.setTimestamp(createOrderDto.getTimestamp());

        return warehouseService.createOrder(orderModel);
    }
}
