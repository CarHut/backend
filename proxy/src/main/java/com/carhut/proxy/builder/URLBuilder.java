package com.carhut.proxy.builder;

import static com.carhut.proxy.builder.enums.DestinationPrefix.*;

import com.carhut.proxy.util.logger.ProxyLogger;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Scope;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

// In future add mappings to database
public class URLBuilder {

    private static final ProxyLogger logger = ProxyLogger.getInstance();

    public static URI buildRequestUrl(HttpServletRequest request) throws URISyntaxException {
        if (request == null) {
            return null;
        }
        String servletPath = request.getServletPath();
        String address = getAddressFromServletPath(servletPath);
        String port = getPortFromServletPath(servletPath);
        if (address == null || port == null) {
            logger.logError("One of the URI parts is null. Cannot build URI.");
            throw new URISyntaxException("One of the URI parts is null.", "Invalid URI.");
        }
        return new URI(address + ":" + port + servletPath);
    }

    private static String getPortFromServletPath(String servletPath) {
        logger.logInfo("Getting port from servlet path: " + servletPath);
        return assignPortToStringPrefix(
                Arrays.stream(servletPath.split("/"))
                        .filter(str -> !str.isEmpty())
                        .findFirst()
                        .orElse(null)
        );
    }

    private static String assignPortToStringPrefix(String prefix) {
        return switch (prefix) {
            case IMAGE_SERVICE          -> "8006";
            case MAIL_SERVICE           -> "8007";
            case RATING_SERVICE         -> "8002";
            case SAVED_CARS_SERVICE     -> "8003";
            case SAVED_SEARCHES_SERVICE -> "8004";
            case CARHUT                 -> "8001";
            default -> null;
        };
    }

    private static String getAddressFromServletPath(String servletPath) {
        logger.logInfo("Getting address from servlet path: " + servletPath);
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
            case CARHUT                 -> "http://localhost";
            default -> null;
        };
    }

}
