package com.esentri.tracing.boundary.services.rest;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.java.Log;

@Provider
@Log
public final class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

  @Override
  public Response toResponse(RuntimeException ex) {
    log.severe(String.format("ERROR: %s", ex.getMessage()));

    String errorMessage = ex.getMessage();

    return Response.status(Status.INTERNAL_SERVER_ERROR)
        .type(MediaType.APPLICATION_JSON_TYPE)
        .entity(errorMessage)
        .build();
  }
}
