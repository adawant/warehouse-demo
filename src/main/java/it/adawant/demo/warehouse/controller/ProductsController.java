package it.adawant.demo.warehouse.controller;


import it.adawant.demo.warehouse.command.product.CreateProductCommand;
import it.adawant.demo.warehouse.command.product.GetProductByIdCommand;
import it.adawant.demo.warehouse.command.product.GetProductsCommand;
import it.adawant.demo.warehouse.command.product.UpdateProductCommand;
import it.adawant.demo.warehouse.dto.CreateProductDto;
import it.adawant.demo.warehouse.dto.UpdateProductDto;
import it.adawant.demo.warehouse.mapper.ProductMapper;
import it.adawant.demo.warehouse.resource.ProductResource;
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
        value = {"/warehouse/products"},
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
@RequiredArgsConstructor
@Slf4j
public class ProductsController extends PagedController {

    private final BeanFactory beanFactory;
    private final ProductMapper productMapper;

    @PostMapping
    private ResponseEntity<ProductResource> createProduct(@RequestBody @Valid CreateProductDto createProductDto) {
        log.debug("Received create product request: {}", createProductDto);
        val command = beanFactory.getBean(CreateProductCommand.class, createProductDto);
        val product = command.execute();
        val productResource = productMapper.modelToResource(product);
        log.debug("Responding with created product: {}", productResource);
        return ResponseEntity.ok(productResource);
    }

    @GetMapping
    private ResponseEntity<Iterable<ProductResource>> getProducts(@RequestParam(value = "page", required = false) Integer page,
                                                                  @RequestParam(value = "size", required = false) Integer size,
                                                                  @RequestParam(value = "sort", required = false) String sortProperty,
                                                                  @RequestParam(value = "order", required = false) Sort.Direction direction) {
        val pageable = extractPageable(page, size, sortProperty, direction);

        if (pageable.isPaged()) {
            log.debug("Received paged getProducts request: {}", pageable);
        } else
            log.debug("Received getProducts request");

        val command = beanFactory.getBean(GetProductsCommand.class, pageable);
        val products = command.execute();
        val productResources = products.map(productMapper::modelToResource);
        val response = pageable.isPaged() ? productResources : productResources.getContent();
        log.debug("Responding to getProducts request: {}", response);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    private ResponseEntity<ProductResource> getProductById(@PathVariable Long id) {
        log.debug("Received getProductById request: {}", id);
        val command = beanFactory.getBean(GetProductByIdCommand.class, id);
        val product = command.execute();
        val productResource = productMapper.modelToResource(product);
        log.debug("Responding with product {}", productResource);
        return ResponseEntity.ok(productResource);
    }

    @PatchMapping("/{id}")
    private ResponseEntity<ProductResource> updateProduct(@PathVariable Long id,
                                                          @RequestBody @Valid UpdateProductDto updateProductDto) {
        log.debug("Received update product request for {}: {}", id, updateProductDto);
        val command = beanFactory.getBean(UpdateProductCommand.class, id, updateProductDto);
        val product = command.execute();
        val productResource = productMapper.modelToResource(product);
        log.debug("Responding with updated product: {}", productResource);
        return ResponseEntity.ok(productResource);
    }

}
