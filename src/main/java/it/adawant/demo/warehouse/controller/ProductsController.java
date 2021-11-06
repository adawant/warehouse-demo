package it.adawant.demo.warehouse.controller;


import it.adawant.demo.warehouse.command.product.CreateProductCommand;
import it.adawant.demo.warehouse.command.product.GetProductByIdCommand;
import it.adawant.demo.warehouse.command.product.GetProductsCommand;
import it.adawant.demo.warehouse.command.product.UpdateProductCommand;
import it.adawant.demo.warehouse.dto.CreateProductDto;
import it.adawant.demo.warehouse.dto.UpdateProductDto;
import it.adawant.demo.warehouse.mapper.ProductMapper;
import it.adawant.demo.warehouse.resource.ProductResource;
import lombok.val;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(
        value = {"/warehouse/products"},
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
public class ProductsController {

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private ProductMapper productMapper;

    @PostMapping
    private ResponseEntity<ProductResource> createProduct(@RequestBody @Valid CreateProductDto createProductDto) {
        val command = beanFactory.getBean(CreateProductCommand.class, createProductDto);
        val product = command.execute();
        val productResource = productMapper.modelToResource(product);
        return ResponseEntity.ok(productResource);
    }

    @GetMapping
    private ResponseEntity<List<ProductResource>> getProducts() {
        val command = beanFactory.getBean(GetProductsCommand.class);
        val products = command.execute();
        val productResources = products.stream()
                .map(productMapper::modelToResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productResources);
    }

    @GetMapping("/{id}")
    private ResponseEntity<ProductResource> getProductById(@PathVariable String id) {
        val command = beanFactory.getBean(GetProductByIdCommand.class, id);
        val product = command.execute();
        val productResource = productMapper.modelToResource(product);
        return ResponseEntity.ok(productResource);
    }

    @PutMapping("/{id}")
    private ResponseEntity<ProductResource> updateProduct(@PathVariable String id,
                                                          @RequestBody @Valid UpdateProductDto updateProductDto) {
        val command = beanFactory.getBean(UpdateProductCommand.class, id, updateProductDto);
        val product = command.execute();
        val productResource = productMapper.modelToResource(product);
        return ResponseEntity.ok(productResource);
    }


}
