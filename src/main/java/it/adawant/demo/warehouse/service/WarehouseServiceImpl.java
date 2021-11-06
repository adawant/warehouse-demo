package it.adawant.demo.warehouse.service;

import it.adawant.demo.warehouse.mapper.OrderMapper;
import it.adawant.demo.warehouse.mapper.ProductMapper;
import it.adawant.demo.warehouse.model.OrderModel;
import it.adawant.demo.warehouse.model.ProductModel;
import it.adawant.demo.warehouse.repository.OrderRepository;
import it.adawant.demo.warehouse.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;

    @Override
    public OrderModel createOrder(OrderModel orderModel) {
        val sum = orderModel.getProducts()
                .stream().map(ProductModel::getPrice).
                reduce(BigDecimal.ZERO, BigDecimal::add);
        orderModel.setTotal(sum);

        var orderEntity = orderMapper.modelToEntity(orderModel);
        orderEntity = orderRepository.save(orderEntity);
        return orderMapper.entityToModel(orderEntity);
    }

    @Override
    public Page<OrderModel> getOrders(Pageable pageable) {
        val entities = orderRepository.findAll(pageable);
        return entities.map(orderMapper::entityToModel);
    }

    @Override
    public ProductModel createProduct(ProductModel productModel) {
        var productEntity = productMapper.modelToEntity(productModel);
        productEntity = productRepository.save(productEntity);
        return productMapper.entityToModel(productEntity);
    }

    @Override
    public ProductModel getProductById(Long id) {
        val entity = productRepository.findById(id).orElseThrow();
        return productMapper.entityToModel(entity);
    }

    @Override
    public Page<ProductModel> getProducts(Pageable pageable) {
        val entities = productRepository.findAll(pageable);
        return entities.map(productMapper::entityToModel);
    }

    @Override
    public List<ProductModel> getProductsById(List<Long> productIds) {
        val entities = productRepository.findAllById(productIds);
        return productMapper.entitiesToModel(entities);
    }

    @Override
    public ProductModel updateProduct(Long id, ProductModel productModel) {
        val entity = productRepository.findById(id).orElseThrow();
        if (productModel.getPrice() != null)
            entity.setPrice(productModel.getPrice());
        if (productModel.getName() != null)
            entity.setName(productModel.getName());

        productRepository.save(entity);

        return productMapper.entityToModel(entity);
    }
}
