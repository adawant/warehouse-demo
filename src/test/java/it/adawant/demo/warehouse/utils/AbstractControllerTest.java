package it.adawant.demo.warehouse.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.adawant.demo.warehouse.BaseSpringTest;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public abstract class AbstractControllerTest extends BaseSpringTest {

    @Autowired
    protected ObjectMapper objectMapper;

    protected <T> T basicGet(TypeReference<T> responseTarget, String endpoint,
                             ResultMatcher... resultMatchers) throws Exception {
        return basicRequest(responseTarget, get(endpoint), resultMatchers);
    }

    protected <T> T basicPost(TypeReference<T> responseTarget, String endpoint, Object requestContent,
                              ResultMatcher... resultMatchers) throws Exception {
        val requestBuilder = addContent(post(endpoint), requestContent);
        return basicRequest(responseTarget, requestBuilder, resultMatchers);
    }

    protected <T> T basicPatch(TypeReference<T> responseTarget, String endpoint, Object requestContent,
                               ResultMatcher... resultMatchers) throws Exception {
        val requestBuilder = addContent(patch(endpoint), requestContent);
        return basicRequest(responseTarget, requestBuilder, resultMatchers);
    }

    private RequestBuilder addContent(MockHttpServletRequestBuilder requestBuilder, Object requestContent) throws JsonProcessingException {
        return requestBuilder.contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(requestContent)
                );
    }


    protected <T> T basicRequest(TypeReference<T> responseTarget, RequestBuilder requestBuilder, ResultMatcher... resultMatchers) throws Exception {
        var request = mockMvc.perform(requestBuilder);
        for (val resultMatcher : resultMatchers) {
            request = request.andExpect(resultMatcher);
        }
        val response = request.andReturn().getResponse().getContentAsByteArray();
        return response.length == 0 ? null : objectMapper.readValue(response, responseTarget);
    }
}
