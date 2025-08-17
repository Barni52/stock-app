package org.example.stock.config.jwt;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // Ensure this filter runs first
public class DebugRequestHeadersFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        System.out.println("\n--- DebugRequestHeadersFilter: Incoming Request ---");
        System.out.println("URL: " + httpRequest.getRequestURL());
        System.out.println("Method: " + httpRequest.getMethod());
        System.out.println("Headers:");

        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            Enumeration<String> headers = httpRequest.getHeaders(headerName);
            while (headers.hasMoreElements()) {
                String headerValue = headers.nextElement();
                System.out.println("  " + headerName + ": " + headerValue);
            }
        }
        System.out.println("--- End DebugRequestHeadersFilter ---\n");

        chain.doFilter(request, response);
    }
}
