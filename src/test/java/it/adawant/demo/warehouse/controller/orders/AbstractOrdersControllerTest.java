package it.adawant.demo.warehouse.controller.orders;

import com.fasterxml.jackson.core.type.TypeReference;
import it.adawant.demo.warehouse.dto.CreateOrderDto;
import it.adawant.demo.warehouse.dto.CreateProductDto;
import it.adawant.demo.warehouse.repository.OrderRepository;
import it.adawant.demo.warehouse.resource.OrderResource;
import it.adawant.demo.warehouse.resource.ProductResource;
import it.adawant.demo.warehouse.utils.AbstractPagedControllerTest;
import it.adawant.demo.warehouse.utils.DtoProvider;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class AbstractOrdersControllerTest extends AbstractPagedControllerTest<OrderRepository> {

    @Autowired
    private OrderRepository orderRepository;

    protected List<Long> availableProductsId = new ArrayList<>();


    @Override
    protected OrderRepository getRepository() {
        return orderRepository;
    }

    protected static final String BASE_URL = "/warehouse/orders";
    protected static final String PRODUCTS_URL = "/warehouse/products";


    @SneakyThrows
    protected OrderResource createOrder(CreateOrderDto dto, ResultMatcher... resultMatchers) {
        return basicPost(
                new TypeReference<>() {
                },
                BASE_URL,
                dto,
                resultMatchers);
    }

    @SneakyThrows
    protected OrderResource getOrderById(Long id, ResultMatcher... resultMatchers) {
        return basicGet(
                new TypeReference<>() {
                },
                BASE_URL + "/" + id,
                resultMatchers);
    }

    @SneakyThrows
    protected List<OrderResource> getOrders(ResultMatcher... resultMatchers) {
        return testPagedAndUnpagedGet(
                new TypeReference<>() {
                },
                BASE_URL, resultMatchers
        );
    }

    @Before
    public void initDb() throws Exception {
        for (int i = 0; i < 10; i++) {
            createProduct(DtoProvider.createProductDtoA());
            createProduct(DtoProvider.createProductDtoB());
            createProduct(DtoProvider.createProductDtoC());
        }
    }

    protected void createProduct(CreateProductDto dto) throws Exception {
        val res = basicPost(
                new TypeReference<ProductResource>() {
                },
                PRODUCTS_URL,
                dto,
                status().is2xxSuccessful());
        availableProductsId.add(res.getId());
    }

    @SneakyThrows
    protected ProductResource getProductById(Long id, ResultMatcher... resultMatchers) {
        return basicGet(
                new TypeReference<>() {
                },
                PRODUCTS_URL + "/" + id,
                resultMatchers);
    }

}
