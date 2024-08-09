package com.esentri.tracing.boundary.clients.serviceb;

import com.esentri.tracing.entity.Extension;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/ServiceB")
@RegisterRestClient(configKey = "serviceb-client")
public interface ServiceBRestClient {

  @POST
  @Path("/process")
  @Retry(maxRetries = 2, delay = 1000)
  public String processRequest(@QueryParam("id") String id, Extension data);
}
