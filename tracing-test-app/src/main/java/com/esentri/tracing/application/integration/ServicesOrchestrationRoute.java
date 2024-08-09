package com.esentri.tracing.application.integration;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public final class ServicesOrchestrationRoute extends RouteBuilder {

    public static final String ROUTE_ID = ServicesOrchestrationRoute.class.getName();
    public static final String ROUTE_URL = "direct:" + ROUTE_ID;

    @Override
    public void configure() throws Exception {
        
        from(ROUTE_URL)
             .routeId(ROUTE_ID)
             .log(LoggingLevel.INFO, "START: " + ROUTE_ID + " with id=${header.breadcrumbId} called...")
             .setProperty("payload", simple("${body}"))

             .bean("ServiceA", "processRequest(${header.breadcrumbId}, ${exchangeProperty.payload})")
             .log(LoggingLevel.INFO, "Response from A= ${body}.")

             .bean("ServiceB", "processRequest(${header.breadcrumbId}, ${exchangeProperty.payload})")
             .log(LoggingLevel.INFO, "Response from B= ${body}.")

             .log(LoggingLevel.INFO, "END: " + ROUTE_ID);    }
}
