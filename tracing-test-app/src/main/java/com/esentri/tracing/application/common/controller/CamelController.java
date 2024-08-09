package com.esentri.tracing.application.common.controller;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;

@ApplicationScoped
public class CamelController {

  @Inject private Tracer tracer;
  private ProducerTemplate producerTemplate;

  @Inject
  CamelController(ProducerTemplate producerTemplate) {
    this.producerTemplate = producerTemplate;
  }

  public void startCamelRouteSend(String camelRouteUri, Object body) throws Exception {
    producerTemplate.sendBody(camelRouteUri, body);
  }

  public void startCamelRouteSendHeaders(
      String camelRouteUri, Object body, Map<String, Object> headers) throws Exception {
    producerTemplate.sendBodyAndHeaders(camelRouteUri, body, headers);
  }

  public void startCamelRouteSendProperties(
      String camelRouteUri,
      Object body,
      Map<String, Object> headers,
      Map<String, Object> properties) {

    producerTemplate.send(
        camelRouteUri,
        exchange -> {
          exchange.getIn().setBody(body);
          exchange.getIn().setHeaders(headers);
          properties.forEach(exchange::setProperty);
        });
  }

  public Object startCamelRouteRequest(String camelRouteUri, Object body) {
    return producerTemplate.requestBody(camelRouteUri, body);
  }

  public Object startCamelRouteRequest(
      String camelRouteUri, Object body, Map<String, Object> headers) {

    Span span =
        tracer
            .spanBuilder("My custom camel span")
            .setAttribute("camel.route.uri", camelRouteUri)
            .setParent(Context.current().with(Span.current()))
            .setSpanKind(SpanKind.INTERNAL)
            .startSpan();

    Object response = producerTemplate.requestBodyAndHeaders(camelRouteUri, body, headers);

    span.end();

    return response;
  }

  public Exchange startCamelRouteRequest(
      String camelRouteUri,
      Object body,
      Map<String, Object> headers,
      Map<String, Object> properties) {
    Exchange result =
        producerTemplate.request(
            camelRouteUri,
            exchange -> {
              exchange.getIn().setBody(body);
              exchange.getIn().setHeaders(headers);
              properties.forEach(exchange::setProperty);
            });

    return result;
  }
}
