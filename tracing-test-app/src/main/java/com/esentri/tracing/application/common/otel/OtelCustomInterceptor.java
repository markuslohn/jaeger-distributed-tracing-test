package com.esentri.tracing.application.common.otel;

import io.opentelemetry.api.trace.Span;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import lombok.extern.java.Log;

/**
 * Siehe
 * https://stackoverflow.com/questions/73558377/how-to-show-http-response-in-opentelemetry-jaeger
 */
@Provider
@Log
public class OtelCustomInterceptor implements ContainerRequestFilter {

  @Inject Span span;

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    log.fine(
        "OtelCustomInterceptor (Span=%s) called.".formatted(span.getSpanContext().getSpanId()));

    var headers = requestContext.getHeaders();
    headers.forEach((k, l) -> l.forEach(header -> setSpan(k, header, span)));
  }

  private static Span setSpan(String k, String header, Span span) {
    return span.setAttribute("http.request.headers.%s".formatted(k), header);
  }
}
