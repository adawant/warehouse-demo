package it.adawant.demo.warehouse.controller.orders;

import it.adawant.demo.warehouse.dto.CreateOrderDto;
import it.adawant.demo.warehouse.utils.DtoProvider;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BadOrdersControllerTest extends AbstractOrdersControllerTest {

    @Test
    public void testBadOrderCreation() {
        Assertions.assertNull(createOrder(new CreateOrderDto(null, null, null), status().is(400)));
        Assertions.assertNull(createOrder(new CreateOrderDto(Collections.emptyList(), null, null), status().is(400)));
        Assertions.assertNull(createOrder(new CreateOrderDto(null, "me@me", null), status().is(400)));
        Assertions.assertNull(createOrder(new CreateOrderDto(Collections.emptyList(), "te@te", null), status().is(400)));

        Assertions.assertNull(createOrder(new CreateOrderDto(null, null, Instant.now()), status().is(400)));
        Assertions.assertNull(createOrder(new CreateOrderDto(Collections.emptyList(), null, Instant.now()), status().is(400)));
        Assertions.assertNull(createOrder(new CreateOrderDto(null, "me@me", Instant.now()), status().is(400)));
        Assertions.assertNull(createOrder(new CreateOrderDto(Collections.emptyList(), "te@te", Instant.now()), status().is(400)));


        Assertions.assertNull(createOrder(new CreateOrderDto(availableProductsId, null, null), status().is(400)));
        Assertions.assertNull(createOrder(new CreateOrderDto(availableProductsId, "notAnEmail", null), status().is(400)));

        Assertions.assertNull(createOrder(new CreateOrderDto(availableProductsId, null, Instant.now()), status().is(400)));
        Assertions.assertNull(createOrder(new CreateOrderDto(availableProductsId, "notAnEmail", Instant.now()), status().is(400)));

        Assertions.assertNull(createOrder(DtoProvider.createOrderDto(List.of(1000L), Instant.now()), status().is(404)));

    }


    @Test
    public void testBadPagedRequest() throws Exception {
        for (int i = 0; i < 40; i++)
            createOrder(DtoProvider.createOrderDto(availableProductsId, null), status().is2xxSuccessful());

        Assertions.assertEquals(40, getOrders(status().is2xxSuccessful()).size());

        testBadPagedRequests(BASE_URL);
    }

}
