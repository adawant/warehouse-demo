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
        value = {"/warehouse/products"},
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
public class ProductsController extends PagedController {

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
    private ResponseEntity<Iterable<ProductResource>> getProducts(@RequestParam(value = "page", required = false) Integer page,
                                                                  @RequestParam(value = "size", required = false) Integer size,
                                                                  @RequestParam(value = "sort", required = false) String sortProperty,
                                                                  @RequestParam(value = "order", required = false) Sort.Direction direction) {
        val pageable = extractPageable(page, size, sortProperty, direction);
        val command = beanFactory.getBean(GetProductsCommand.class, pageable);
        val products = command.execute();
        val productResources = products.map(productMapper::modelToResource);
        return ResponseEntity.ok(pageable.isPaged() ? productResources : productResources.getContent());
    }


    @GetMapping("/{id}")
    private ResponseEntity<ProductResource> getProductById(@PathVariable Long id) {
        val command = beanFactory.getBean(GetProductByIdCommand.class, id);
        val product = command.execute();
        val productResource = productMapper.modelToResource(product);
        return ResponseEntity.ok(productResource);
    }

    @PatchMapping("/{id}")
    private ResponseEntity<ProductResource> updateProduct(@PathVariable Long id,
                                                          @RequestBody @Valid UpdateProductDto updateProductDto) {
        val command = beanFactory.getBean(UpdateProductCommand.class, id, updateProductDto);
        val product = command.execute();
        val productResource = productMapper.modelToResource(product);
        return ResponseEntity.ok(productResource);
    }

}
