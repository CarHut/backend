package com.carhut.proxy.builders;

import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import static com.carhut.proxy.models.enums.DestinationURI.*;
public class URIBuilder {

    public static URI buildURIRoute(HttpServletRequest request) throws URISyntaxException {
        String servletPath = request.getServletPath();
        String address = getAddressFromServletPath(servletPath);
        String port = getPortFromServletPath(servletPath);
        return new URI(address + ":" + port + servletPath);
    }

    private static String getPortFromServletPath(String servletPath) {
        return assignPortToStringPrefix(
                Arrays.stream(servletPath.split("/"))
                        .filter(str -> !str.isEmpty())
                        .findFirst()
                        .orElse(null)
        );
    }

    private static String assignPortToStringPrefix(String prefix) {
        return switch (prefix) {
            case IMAGE_SERVICE          -> "8000";
            case MAIL_SERVICE           -> "8001";
            case RATING_SERVICE         -> "8002";
            case SAVED_CARS_SERVICE     -> "8003";
            case SAVED_SEARCHES_SERVICE -> "8004";
            default -> null;
        };
    }

    private static String getAddressFromServletPath(String servletPath) {
        return assignAddressToStringPrefix(
                Arrays.stream(servletPath.split("/"))
                        .filter(str -> !str.isEmpty())
                        .findFirst()
                        .orElse(null)
        );
    }


    private static String assignAddressToStringPrefix(String prefix) {
        return switch (prefix) {
            case IMAGE_SERVICE          -> "http://localhost";
            case MAIL_SERVICE           -> "http://localhost";
            case RATING_SERVICE         -> "http://localhost";
            case SAVED_CARS_SERVICE     -> "http://localhost";
            case SAVED_SEARCHES_SERVICE -> "http://localhost";
            default -> null;
        };
    }

}
