package it.adawant.demo.warehouse.controller;


import it.adawant.demo.warehouse.command.order.CreateOrderCommand;
import it.adawant.demo.warehouse.command.order.GetOrdersCommand;
import it.adawant.demo.warehouse.dto.CreateOrderDto;
import it.adawant.demo.warehouse.mapper.OrderMapper;
import it.adawant.demo.warehouse.resource.OrderResource;
import it.adawant.demo.warehouse.utils.PagedController;
import lombok.val;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(
        value = {"/warehouse/orders"},
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
public class OrdersController extends PagedController {

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private OrderMapper orderMapper;


    @PostMapping
    private ResponseEntity<OrderResource> createOrder(@RequestBody @Valid CreateOrderDto createOrderDto)  {
        val command = beanFactory.getBean(CreateOrderCommand.class, createOrderDto);
        val order = command.execute();
        val orderResource = orderMapper.modelToResource(order);
        return ResponseEntity.ok(orderResource);
    }

    @GetMapping
    private ResponseEntity<Iterable<OrderResource>> getOrders(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort", required = false) String sortProperty,
            @RequestParam(value = "order", required = false) Sort.Direction direction
    ) {
        val pageable = extractPageable(page, size, sortProperty, direction);
        val command = beanFactory.getBean(GetOrdersCommand.class, pageable);
        val orders = command.execute();
        val orderResourcesPaged = orders.map(orderMapper::modelToResource);
        return ResponseEntity.ok(pageable.isPaged() ? orderResourcesPaged : orderResourcesPaged.getContent());
    }

}
