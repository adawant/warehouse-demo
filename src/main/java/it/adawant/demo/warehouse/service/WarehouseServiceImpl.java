package it.adawant.demo.warehouse.service;

import it.adawant.demo.warehouse.mapper.OrderMapper;
import it.adawant.demo.warehouse.mapper.ProductMapper;
import it.adawant.demo.warehouse.model.OrderModel;
import it.adawant.demo.warehouse.model.ProductModel;
import it.adawant.demo.warehouse.repository.OrderRepository;
import it.adawant.demo.warehouse.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;

    @Override
    public ProductModel createProduct(ProductModel productModel) {
        log.info("Creating product {} at price {}", productModel.getName(), productModel.getPrice());
        var productEntity = productMapper.modelToEntity(productModel);
        productEntity = productRepository.save(productEntity);
        log.info("Product {} with id {} created", productEntity.getName(), productEntity.getId());
        return productMapper.entityToModel(productEntity);
    }

    @Override
    public ProductModel getProductById(Long id) {
        log.info("Retrieving product {}", id);
        val entity = productRepository.findById(id).orElseThrow();
        return productMapper.entityToModel(entity);
    }

    @Override
    public Page<ProductModel> getProducts(Pageable pageable) {
        log.info("Retriving all products");
        val entities = productRepository.findAll(pageable);
        log.info("Retrieved {} products", entities.getTotalElements());
        return entities.map(productMapper::entityToModel);
    }

    @Override
    public List<ProductModel> getProductsById(Set<Long> productIds) {
        log.info("Retrieving products : {}", productIds);
        val entities = productRepository.findAllById(productIds);

        if (IterableUtils.size(entities) != productIds.size())
            throw new NoSuchElementException("Some elements are missing!");

        return productMapper.entitiesToModel(entities);
    }

    @Override
    public ProductModel updateProduct(ProductModel productModel) {
        log.info("Updating product {}", productModel.getId());
        val entity = productRepository.findById(productModel.getId()).orElseThrow();

        if (productModel.getPrice() != null) {
            val oldVal = entity.getPrice();
            entity.setPrice(productModel.getPrice());
            log.debug("Product {} changed its price from {} to {}", entity.getId(), oldVal, entity.getPrice());
        }

        if (productModel.getName() != null) {
            val oldVal = entity.getName();
            entity.setName(productModel.getName());
            log.debug("Product {} changed its name from {} to {}", entity.getId(), oldVal, entity.getName());
        }

        productRepository.save(entity);
        log.info("Product {} updated", productModel.getId());
        return productMapper.entityToModel(entity);
    }


    @Override
    public OrderModel createOrder(OrderModel orderModel) {
        log.info("Creating order placed by {} at {} with products: {}",
                orderModel.getBuyerEmail(), orderModel.getTimestamp(), orderModel.getProducts());

        val sum = orderModel.getProducts()
                .stream().map(ProductModel::getPrice).
                reduce(BigDecimal.ZERO, BigDecimal::add);
        orderModel.setTotal(sum);

        var orderEntity = orderMapper.modelToEntity(orderModel);
        orderEntity = orderRepository.save(orderEntity);

        log.info("Order {} with a total of {}, placed by {}, created",
                orderEntity.getId(), orderEntity.getTotal(), orderEntity.getBuyerEmail());

        return orderMapper.entityToModel(orderEntity);
    }

    @Override
    public OrderModel getOrderById(Long id) {
        log.info("Retrieving order {}", id);
        val entity = orderRepository.findById(id).orElseThrow();
        return orderMapper.entityToModel(entity);
    }

    @Override
    public Page<OrderModel> getOrders(Pageable pageable) {
        log.info("Retrieving all orders");
        val entities = orderRepository.findAll(pageable);
        log.info("Retrieved {} orders", entities.getTotalElements());
        return entities.map(orderMapper::entityToModel);
    }
}
