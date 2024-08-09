package com.esentri.tracing.boundary.services.rest;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.java.Log;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;

@Provider
@Log
public final class CamelExcecutionExceptionMapper
    implements ExceptionMapper<CamelExecutionException> {

  @Override
  public Response toResponse(CamelExecutionException ex) {
    Throwable cause = findKnownCause(ex);

    log.severe(
        String.format(
            "============ ERROR-%s: ============ %s%s",
            getBreadcrumbId(ex), System.lineSeparator(), cause.getMessage()));

    String errorMessage = cause.getMessage();

    return Response.status(Status.INTERNAL_SERVER_ERROR)
        .type(MediaType.APPLICATION_JSON_TYPE)
        .entity(errorMessage)
        .build();
  }

  private Throwable findKnownCause(CamelExecutionException ex) {
    Throwable lastFoundCause = ex;
    while (lastFoundCause.getCause() != null) {
      lastFoundCause = lastFoundCause.getCause();
    }
    return ex;
  }

  private String getBreadcrumbId(CamelExecutionException ex) {
    String propertyValue = ex.getExchange().getProperty(Exchange.BREADCRUMB_ID, String.class);
    if (propertyValue == null) {
      propertyValue = ex.getExchange().getMessage().getHeader(Exchange.BREADCRUMB_ID, String.class);
    }
    return propertyValue;
  }
}
