package it.adawant.demo.warehouse.controller.products;

import it.adawant.demo.warehouse.dto.CreateProductDto;
import it.adawant.demo.warehouse.dto.UpdateProductDto;
import it.adawant.demo.warehouse.resource.ProductResource;
import it.adawant.demo.warehouse.utils.DtoProvider;
import lombok.val;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.util.function.Supplier;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BasicProductsControllerTest extends AbstractProductsControllerTest {


    @Test
    public void basicFlowTest() throws Exception {
        var products = getProducts(status().is2xxSuccessful());
        Assertions.assertTrue(products.isEmpty());

        //Insert an element
        var firstRes = testIncrementalInsert(DtoProvider.createProductDtoA());

        //Insert another element equal to the first one
        var secondRes = testIncrementalInsert(DtoProvider.createProductDtoA(), firstRes);
        Assertions.assertNotEquals(secondRes.getId(), firstRes.getId());


        //Insert another element
        var thirdRes = testIncrementalInsert(DtoProvider.createProductDtoB(), firstRes, secondRes);

        //Insert another element
        var fourthRes = testIncrementalInsert(DtoProvider.createProductDtoC(), firstRes, secondRes, thirdRes);

        //Test first element price update
        var firstUpdate = new UpdateProductDto(null, firstRes.getPrice().add(BigDecimal.TEN));
        firstRes = testIncrementalUpdate(firstRes.getId(), firstUpdate, secondRes, thirdRes, fourthRes);

        //Test second element name update
        val secondUpdate = new UpdateProductDto("another name", null);
        secondRes = testIncrementalUpdate(secondRes.getId(), secondUpdate, firstRes, thirdRes, fourthRes);

        //Test first element name update
        val firstUpdateBis = new UpdateProductDto("qwertyuiop", null);
        firstRes = testIncrementalUpdate(firstRes.getId(), firstUpdateBis, secondRes, thirdRes, fourthRes);

        //Test third element price update
        val thirdUpdate = new UpdateProductDto(null, thirdRes.getPrice().subtract(BigDecimal.ONE));
        testIncrementalUpdate(thirdRes.getId(), thirdUpdate, firstRes, secondRes, fourthRes);
    }


    private ProductResource testIncrementalInsert(CreateProductDto toInsert, ProductResource... alreadyExpected) throws Exception {
        return testIncremental(() -> {
            try {
                return testBaseProductCreation(toInsert);
            } catch (Exception e) {
                assert false : e;
                throw new RuntimeException(e);
            }
        }, alreadyExpected);
    }

    private ProductResource testIncrementalUpdate(Long toUpdate, UpdateProductDto updateProductDto, ProductResource... alreadyExpected) throws Exception {
        return testIncremental(() -> testBaseProductUpdate(toUpdate, updateProductDto), alreadyExpected);
    }

    private ProductResource testIncremental(Supplier<ProductResource> action, ProductResource... alreadyExpected) {
        return testIncremental(
                action,
                res -> getProductById(res.getId(), status().is2xxSuccessful()),
                () -> getProducts(status().is2xxSuccessful()),
                alreadyExpected
        );
    }

    private ProductResource testBaseProductUpdate(Long id, UpdateProductDto dto) {
        val previousRes = getProductById(id, status().is2xxSuccessful());
        val res = updateProduct(id, dto, status().is2xxSuccessful());
        Assertions.assertNotEquals(previousRes, res);
        Assertions.assertNotNull(res.getId());

        if (dto.getName() != null)
            Assertions.assertNotEquals(previousRes.getName(), res.getName());
        else
            Assertions.assertEquals(previousRes.getName(), res.getName());

        if (dto.getPrice() != null)
            Assertions.assertNotEquals(previousRes.getPrice(), res.getPrice());
        else
            Assertions.assertEquals(previousRes.getPrice(), res.getPrice());

        return res;
    }

    private ProductResource testBaseProductCreation(CreateProductDto dto) throws Exception {
        val res = createProduct(dto, status().is2xxSuccessful());
        Assertions.assertEquals(res.getName(), dto.getName());
        Assertions.assertEquals(res.getPrice(), dto.getPrice());
        Assertions.assertNotNull(res.getId());
        return res;
    }


}
