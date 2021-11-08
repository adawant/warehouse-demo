package it.adawant.demo.warehouse.controller.orders;

import it.adawant.demo.warehouse.dto.CreateOrderDto;
import it.adawant.demo.warehouse.resource.OrderResource;
import it.adawant.demo.warehouse.resource.ProductResource;
import it.adawant.demo.warehouse.utils.CollectionUtils;
import it.adawant.demo.warehouse.utils.DtoProvider;
import lombok.val;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.function.Supplier;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BasicOrdersControllerTest extends AbstractOrdersControllerTest {

    @Test
    public void testBasicFlow() throws Exception {
        var orders = getOrders(status().is2xxSuccessful());
        Assertions.assertTrue(orders.isEmpty());

        //Insert an element
        var firstRes = testIncrementalInsert(DtoProvider.createOrderDto(availableProductsId.subList(0, 10), null));

        //Insert another element equal to the first one
        var secondRes = testIncrementalInsert(DtoProvider.createOrderDto(availableProductsId.subList(0, 10), null), firstRes);
        Assertions.assertNotEquals(secondRes.getId(), firstRes.getId());


        //Insert another element
        var thirdRes = testIncrementalInsert(DtoProvider.createOrderDto(availableProductsId.subList(0, 10), Instant.now()), firstRes, secondRes);

        //Insert another element
        var fourthRes = testIncrementalInsert(DtoProvider.createOrderDto(availableProductsId.subList(5, 17), null), firstRes, secondRes, thirdRes);

        //Insert another order with duplicated elements
        val elements = availableProductsId.subList(5, 7);
        val duplicatedElements = new ArrayList<>(elements);
        duplicatedElements.addAll(elements);
        duplicatedElements.addAll(elements);
        var fifthRes = testIncrementalInsert(DtoProvider.createOrderDto(duplicatedElements, null), firstRes, secondRes, thirdRes, fourthRes);


    }

    private OrderResource testBaseOrderCreation(CreateOrderDto dto) {
        val res = createOrder(dto, status().is2xxSuccessful());

        Assertions.assertEquals(dto.getBuyerEmail(), res.getBuyerEmail());
        Assertions.assertTrue(CollectionUtils.hasSameContent(dto.getProductsId(), res.getProductsId()));

        if (dto.getTimestamp() == null)
            Assertions.assertNotNull(res.getTimestamp());
        else
            Assertions.assertEquals(dto.getTimestamp(), res.getTimestamp());

        Assertions.assertNotNull(res.getId());

        val expectedTotal = dto.getProductsId().stream()
                .map(this::getProductById)
                .map(ProductResource::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add).stripTrailingZeros();

        Assertions.assertEquals(expectedTotal, res.getTotal());
        return res;
    }

    private OrderResource testIncrementalInsert(CreateOrderDto toInsert, OrderResource... alreadyExpected) throws Exception {
        return testIncremental(() -> testBaseOrderCreation(toInsert), alreadyExpected);
    }

    private OrderResource testIncremental(Supplier<OrderResource> action, OrderResource... alreadyExpected) {
        return testIncremental(
                action,
                res -> getOrderById(res.getId(), status().is2xxSuccessful()),
                () -> getOrders(status().is2xxSuccessful()),
                alreadyExpected
        );
    }
}
