package com.esentri.tracing.boundary.services.rest;

import com.esentri.tracing.entity.Extension;
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
import lombok.extern.java.Log;

@Log
@Path("/ServiceA")
@ApplicationScoped
public final class ServiceA {

  @POST
  @Path("/process")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  public Response processRequest(
      @Context HttpHeaders headers, @QueryParam("id") String id, Extension data) {

    log.info(String.format("Received id=%s and data=%s at ServiceA", id, data.getName()));

    return Response.ok(String.format("Successfully processed request %s for ServiceA", id)).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/alive")
  public Response checkAlive() {
    return Response.ok().build();
  }
}
