package it.adawant.demo.warehouse.controller;


import it.adawant.demo.warehouse.command.order.CreateOrderCommand;
import it.adawant.demo.warehouse.command.order.GetOrderByIdCommand;
import it.adawant.demo.warehouse.command.order.GetOrdersCommand;
import it.adawant.demo.warehouse.dto.CreateOrderDto;
import it.adawant.demo.warehouse.mapper.OrderMapper;
import it.adawant.demo.warehouse.resource.OrderResource;
import it.adawant.demo.warehouse.utils.PagedController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.BeanFactory;
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
@RequiredArgsConstructor
@Slf4j
public class OrdersController extends PagedController {

    private final BeanFactory beanFactory;
    private final OrderMapper orderMapper;


    @PostMapping
    private ResponseEntity<OrderResource> createOrder(@RequestBody @Valid CreateOrderDto createOrderDto) {
        log.debug("Received create order request: {}", createOrderDto);
        val command = beanFactory.getBean(CreateOrderCommand.class, createOrderDto);
        val order = command.execute();
        val orderResource = orderMapper.modelToResource(order);
        log.debug("Responding with created order: {}", orderResource);
        return ResponseEntity.ok(orderResource);
    }

    @GetMapping("/{id}")
    private ResponseEntity<OrderResource> getOrderById(@PathVariable Long id) {
        log.debug("Received getOrderById request: {}", id);
        val command = beanFactory.getBean(GetOrderByIdCommand.class, id);
        val order = command.execute();
        val orderResource = orderMapper.modelToResource(order);
        log.debug("Responding with order {}", orderResource);
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

        if (pageable.isPaged()) {
            log.debug("Received paged getOrders request: {}", pageable);
        } else
            log.debug("Received getOrders request");

        val command = beanFactory.getBean(GetOrdersCommand.class, pageable);
        val orders = command.execute();
        val orderResourcesPaged = orders.map(orderMapper::modelToResource);
        val response = pageable.isPaged() ? orderResourcesPaged : orderResourcesPaged.getContent();
        log.debug("Responding to getOrders request: {}", response);
        return ResponseEntity.ok(response);
    }

}
