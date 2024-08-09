package com.esentri.tracing.boundary.clients.serviceb;

import com.esentri.tracing.entity.Extension;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
@Named("ServiceB")
@Log
public class ServiceBAdapter {

  @RestClient ServiceBRestClient client;

  public String processRequest(@NonNull String id, @NonNull Extension data) {
    log.info(String.format("Call processRequest(id=%s, data=%s)", id, data.getName()));

    return client.processRequest(id, data);
  }
}
