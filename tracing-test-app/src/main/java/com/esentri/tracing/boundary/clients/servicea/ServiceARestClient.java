package com.esentri.tracing.boundary.clients.servicea;

import com.esentri.tracing.entity.Extension;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/ServiceA")
@RegisterRestClient(configKey = "servicea-client")
public interface ServiceARestClient {

  @POST
  @Path("/process")
  @Retry(maxRetries = 2, delay = 1000)
  public String processRequest(@QueryParam("id") String id, Extension data);
}
