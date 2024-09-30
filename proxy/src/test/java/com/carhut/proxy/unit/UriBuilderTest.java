package com.carhut.proxy.unit;

import com.carhut.proxy.builder.URLBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URISyntaxException;

public class UriBuilderTest {

    @Test
    void buildRequest_requestIsNull() throws URISyntaxException {
        Assertions.assertNull(URLBuilder.buildRequestUrl(null));
    }

    @Test
    void buildRequest_servletHasInvalidPath() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getServletPath()).thenReturn("/invalid-path");
        Assertions.assertThrowsExactly(URISyntaxException.class, () -> URLBuilder.buildRequestUrl(request));
    }

    @Test
    void buildRequest_buildValidRequestForCarhutServer() throws URISyntaxException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getServletPath()).thenReturn("/carhut");
        Assertions.assertEquals("http://localhost:8001/carhut",
                URLBuilder.buildRequestUrl(request).toString());
    }

}
