package it.adawant.demo.warehouse.controller.products;

import it.adawant.demo.warehouse.dto.CreateProductDto;
import it.adawant.demo.warehouse.dto.UpdateProductDto;
import it.adawant.demo.warehouse.utils.DtoProvider;
import lombok.val;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BadProductsControllerTest extends AbstractProductsControllerTest {


    @Test
    public void testBadCreateCalls() throws Exception {
        Assertions.assertNull(createProduct(new CreateProductDto(null, null), status().is(400)));
        Assertions.assertNull(createProduct(new CreateProductDto(null, BigDecimal.ONE), status().is(400)));
        Assertions.assertNull(createProduct(new CreateProductDto("something", null), status().is(400)));
        Assertions.assertNull(createProduct(new CreateProductDto("something", BigDecimal.valueOf(-1)), status().is(400)));
    }

    @Test
    public void testBadUpdateCalls() throws Exception {
        //update not existing product
        Assertions.assertNull(updateProduct(1L, new UpdateProductDto(null, null), status().is(400)));
        Assertions.assertNull(updateProduct(1L, new UpdateProductDto(null, null), status().is(400)));

        Assertions.assertNull(updateProduct(1L, new UpdateProductDto("a", null), status().is(404)));
        Assertions.assertNull(updateProduct(1L, new UpdateProductDto(null, BigDecimal.ONE), status().is(404)));
        Assertions.assertNull(updateProduct(1L, new UpdateProductDto("a", BigDecimal.ONE), status().is(404)));

        val product = createProduct(new CreateProductDto("a", BigDecimal.ONE), status().is2xxSuccessful());

        Assertions.assertNull(updateProduct(product.getId(), new UpdateProductDto(null, null), status().is(400)));
        Assertions.assertNull(updateProduct(product.getId(), new UpdateProductDto("a", BigDecimal.valueOf(-1)), status().is(400)));
        Assertions.assertNotNull(updateProduct(product.getId(), new UpdateProductDto("a", null), status().is2xxSuccessful()));
        Assertions.assertNotNull(updateProduct(product.getId(), new UpdateProductDto(null, BigDecimal.ONE), status().is2xxSuccessful()));
        Assertions.assertNotNull(updateProduct(product.getId(), new UpdateProductDto("a", BigDecimal.ONE), status().is2xxSuccessful()));
    }

    @Test
    public void testBadPagedRequest() throws Exception {
        for (int i = 0; i < 40; i++)
            createProduct(DtoProvider.createProductDtoA(), status().is2xxSuccessful());

        Assertions.assertEquals(40, getProducts(status().is2xxSuccessful()).size());

        testBadPagedRequests(BASE_URL);
    }


}
