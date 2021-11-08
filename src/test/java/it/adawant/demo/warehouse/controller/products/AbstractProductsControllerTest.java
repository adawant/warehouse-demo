package it.adawant.demo.warehouse.controller.products;

import com.fasterxml.jackson.core.type.TypeReference;
import it.adawant.demo.warehouse.dto.CreateProductDto;
import it.adawant.demo.warehouse.dto.UpdateProductDto;
import it.adawant.demo.warehouse.repository.ProductRepository;
import it.adawant.demo.warehouse.resource.ProductResource;
import it.adawant.demo.warehouse.utils.AbstractPagedControllerTest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

public abstract class AbstractProductsControllerTest extends AbstractPagedControllerTest<ProductRepository> {

    protected static final String BASE_URL = "/warehouse/products";

    @Autowired
    private ProductRepository productRepository;

    @Override
    protected ProductRepository getRepository() {
        return productRepository;
    }

    @SneakyThrows
    protected ProductResource createProduct(CreateProductDto dto, ResultMatcher... resultMatchers) {
        return basicPost(
                new TypeReference<>() {
                },
                BASE_URL,
                dto,
                resultMatchers);
    }

    @SneakyThrows
    protected ProductResource updateProduct(Long id, UpdateProductDto updateProductDto, ResultMatcher... resultMatchers) {
        return basicPatch(
                new TypeReference<>() {
                },
                BASE_URL + "/" + id,
                updateProductDto,
                resultMatchers);
    }


    @SneakyThrows
    protected ProductResource getProductById(Long id, ResultMatcher... resultMatchers) {
        return basicGet(
                new TypeReference<>() {
                },
                BASE_URL + "/" + id,
                resultMatchers);
    }

    @SneakyThrows
    protected List<ProductResource> getProducts(ResultMatcher... resultMatchers) {
        return testPagedAndUnpagedGet(
                new TypeReference<>() {
                },
                BASE_URL, resultMatchers
        );
    }

}
