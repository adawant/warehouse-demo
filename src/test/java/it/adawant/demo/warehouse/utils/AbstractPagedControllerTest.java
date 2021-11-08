package it.adawant.demo.warehouse.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import it.adawant.demo.warehouse.resource.ProductResource;
import lombok.val;
import org.junit.After;
import org.junit.jupiter.api.Assertions;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.util.Pair;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class AbstractPagedControllerTest<R extends PagingAndSortingRepository<?, ?>> extends AbstractControllerTest {


    @After
    public void cleanDb() {
        getRepository().deleteAll();
    }

    protected abstract R getRepository();

    /**
     * Compares the paged result of the endpoint with the unpaged one
     */
    protected <T> List<T> testPagedAndUnpagedGet(TypeReference<List<T>> listResponseTarget,
                                                 String endpoint,
                                                 ResultMatcher... resultMatchers) throws Exception {
        val unpagedResult = basicGet(listResponseTarget, endpoint, resultMatchers);
        testLargePagedGet(listResponseTarget, endpoint, unpagedResult, resultMatchers);

        if (!unpagedResult.isEmpty() && unpagedResult.size() % 2 == 0)
            testHalfPagedGet(listResponseTarget, endpoint, unpagedResult, resultMatchers);

        if (!unpagedResult.isEmpty() && unpagedResult.size() % 4 == 0)
            testQuarterPagedGet(listResponseTarget, endpoint, unpagedResult, resultMatchers);

        return unpagedResult;
    }


    /**
     * Compares a larger page (compared with the size of the unpaged result) with the unpaged result
     */
    private <T> void testLargePagedGet(TypeReference<List<T>> listResponseTarget,
                                       String endpoint, List<T> unpagedResult,
                                       ResultMatcher[] resultMatchers) throws Exception {
        //add size to 10 in order to compare paged result even if collection size is 0
        comparePagedAndUnpaged(1, 1, 10, listResponseTarget, endpoint, unpagedResult, resultMatchers);
    }

    /**
     * Splits the upaged result in two halves and compares the pages with the initial result
     */
    private <T> void testHalfPagedGet(TypeReference<List<T>> listResponseTarget,
                                      String endpoint, List<T> unpagedResult,
                                      ResultMatcher[] resultMatchers) throws Exception {
        assert unpagedResult.size() % 2 == 0;
        comparePagedAndUnpaged(1, 2, 0, listResponseTarget, endpoint, unpagedResult, resultMatchers);
        comparePagedAndUnpaged(2, 2, 0, listResponseTarget, endpoint, unpagedResult, resultMatchers);

    }


    /**
     * Splits the upaged result in four quarters and compares the pages with the initial result
     */
    private <T> void testQuarterPagedGet(TypeReference<List<T>> listResponseTarget, String endpoint, List<T> unpagedResult, ResultMatcher[] resultMatchers) throws Exception {
        assert unpagedResult.size() % 4 == 0;
        comparePagedAndUnpaged(1, 4, 0, listResponseTarget, endpoint, unpagedResult, resultMatchers);
        comparePagedAndUnpaged(2, 4, 0, listResponseTarget, endpoint, unpagedResult, resultMatchers);
        comparePagedAndUnpaged(3, 4, 0, listResponseTarget, endpoint, unpagedResult, resultMatchers);
        comparePagedAndUnpaged(4, 4, 0, listResponseTarget, endpoint, unpagedResult, resultMatchers);
    }


    private <T> void comparePagedAndUnpaged(int page, int listDivisionFactor, int addSizeFactor,
                                            TypeReference<List<T>> listResponseTarget, String endpoint,
                                            List<T> unpagedResult, ResultMatcher[] resultMatchers) throws Exception {

        val queryParam = new LinkedMultiValueMap<String, String>();
        queryParam.add("page", String.valueOf(page));
        queryParam.add("size", String.valueOf(addSizeFactor + unpagedResult.size() / listDivisionFactor));

        val pagedResult = basicGetWithQueryParams(
                new TypeReference<Map<String, Object>>() { //Page implementation cannot be deserialized by jackson
                }, endpoint, queryParam, resultMatchers
        );

        val totalElements = Integer.parseInt(pagedResult.get("size").toString());
        if (addSizeFactor > 0) {
            assert addSizeFactor + unpagedResult.size() / listDivisionFactor >= totalElements;
        } else {
            Assertions.assertEquals(unpagedResult.size() / listDivisionFactor, totalElements);
        }

        val elements = (List<Object>) pagedResult.get("content");
        val elementsParsed = objectMapper.readValue(objectMapper.writeValueAsString(elements), listResponseTarget);
        for (val e : elementsParsed) {
            assert unpagedResult.contains(e) : "Element not contained in paged version: " + e;
        }
    }

    protected <T> T basicGetWithQueryParams(TypeReference<T> responseTarget, String endpoint,
                                            MultiValueMap<String, String> queryParams,
                                            ResultMatcher... resultMatchers) throws Exception {
        return basicRequest(responseTarget, get(endpoint).queryParams(queryParams), resultMatchers);
    }

    /**
     * Function used to test incremental insert/update
     *
     * @param incrementalAction    insert or update action
     * @param singleRetrieveAction getById action
     * @param multiRetrieveAction  getAll action
     * @param alreadyExpected      elements already expected to be present
     * @return the element inserted on db
     */
    @SafeVarargs
    protected final <T> T testIncremental(Supplier<T> incrementalAction,
                                          Function<T, T> singleRetrieveAction,
                                          Supplier<List<T>> multiRetrieveAction,
                                          T... alreadyExpected) {
        val res = incrementalAction.get();
        Assertions.assertEquals(res, singleRetrieveAction.apply(res));
        val products = multiRetrieveAction.get();
        Assertions.assertEquals(alreadyExpected.length + 1, products.size());
        Assertions.assertTrue(products.containsAll(Arrays.asList(alreadyExpected)));
        return res;
    }


    protected void testBadPagedRequests(String endpoint) throws Exception {
        callBadPagedRequest(endpoint, Pair.of("page", String.valueOf(-1)));
        callBadPagedRequest(endpoint, Pair.of("size", String.valueOf(-1)));
        callBadPagedRequest(endpoint, Pair.of("size", String.valueOf(0)));
        callBadPagedRequest(endpoint, Pair.of("sort", "unknownProperty"));
        callBadPagedRequest(endpoint, Pair.of("order", "unexpectedEnumValue"));
    }

    @SafeVarargs
    private void callBadPagedRequest(String endpoint, Pair<String, String>... queryParams) throws Exception {
        var queryParam = new LinkedMultiValueMap<String, String>();
        for (val query : queryParams) {
            queryParam.add(query.getFirst(), query.getSecond());
        }

        basicGetWithQueryParams(new TypeReference<List<ProductResource>>() {
        }, endpoint, queryParam, status().is4xxClientError());
    }
}
