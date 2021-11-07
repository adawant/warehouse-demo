package it.adawant.demo.warehouse.command.order;

import it.adawant.demo.warehouse.dto.CreateOrderDto;
import it.adawant.demo.warehouse.model.OrderModel;
import it.adawant.demo.warehouse.model.ProductModel;
import it.adawant.demo.warehouse.service.WarehouseService;
import it.adawant.demo.warehouse.utils.BaseCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
@Slf4j
public class CreateOrderCommand implements BaseCommand<OrderModel> {

    private final CreateOrderDto createOrderDto;

    @Autowired
    private WarehouseService warehouseService;

    @Override
    public OrderModel execute() {
        val countById = createOrderDto.getProductsId()
                .stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        val productsById = warehouseService.getProductsById(countById.keySet())
                .stream().collect(Collectors.toMap(ProductModel::getId, Function.identity()));
        log.debug("Products for order are : {}", productsById.values());

        val linkedProducts = countById.entrySet().stream()
                .flatMap(e ->
                        Collections.nCopies(e.getValue().intValue(), productsById.get(e.getKey())).stream()
                ).collect(Collectors.toList());

        val orderModel = new OrderModel();

        orderModel.setBuyerEmail(createOrderDto.getBuyerEmail());
        orderModel.setProducts(linkedProducts);

        if (createOrderDto.getTimestamp() == null)
            orderModel.setTimestamp(Instant.now());
        else
            orderModel.setTimestamp(createOrderDto.getTimestamp());

        log.debug("Order timestamp is {}", orderModel.getTimestamp());

        return warehouseService.createOrder(orderModel);
    }
}
