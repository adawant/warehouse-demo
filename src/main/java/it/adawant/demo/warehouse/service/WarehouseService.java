package it.adawant.demo.warehouse.service;

import it.adawant.demo.warehouse.model.OrderModel;
import it.adawant.demo.warehouse.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WarehouseService {

    OrderModel createOrder(OrderModel orderModel);

    Page<OrderModel> getOrders(Pageable pageable);

    ProductModel createProduct(ProductModel productModel);

    ProductModel getProductById(Long id);

    Page<ProductModel> getProducts(Pageable pageable);

    List<ProductModel> getProductsById(List<Long> productIds);

    ProductModel updateProduct(Long id, ProductModel productModel);

}
