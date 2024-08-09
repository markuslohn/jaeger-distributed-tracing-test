package com.esentri.tracing.boundary.services.rest;

import com.esentri.tracing.application.common.controller.CamelController;
import com.esentri.tracing.application.integration.ServicesOrchestrationRoute;
import com.esentri.tracing.entity.Extension;
import io.opentelemetry.api.trace.Span;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.java.Log;
import org.apache.camel.Exchange;

@Log
@Path("/transx")
@ApplicationScoped
public final class BusinessTransaction {

  private CamelController camelController;
  private Span span;

  public BusinessTransaction(Span currentSpan, CamelController camelController) {
    this.camelController = camelController;
    this.span = currentSpan;
  }

  @POST
  @Path("/start")
  @Produces(MediaType.TEXT_PLAIN)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response startTransaction(
      @Context HttpHeaders headers, @QueryParam("id") String id, Extension data) {

    /** Könnte mit einem Functional Interface besser gelöst werden. */
    span.setAttribute("http.request.body", data.toString());

    Map<String, Object> camelHeaders = new HashMap<>();
    camelHeaders.put(Exchange.BREADCRUMB_ID, id);

    Object response =
        camelController.startCamelRouteRequest(
            ServicesOrchestrationRoute.ROUTE_URL, data, camelHeaders);

    log.info(response.toString());
    log.info(String.format("============ FINISHED: %s ============ ", id));

    /** Könnte mit einem Functional Interface besser gelöst werden. */
    span.setAttribute("http.response.body", response.toString());

    return Response.ok(response).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/alive")
  public Response checkAlive() {
    return Response.ok().build();
  }
}
