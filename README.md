# Tristributed Tracing with Jaeger Test



## Installationsschritte

### Installation von Jeager (all-in-one)

Die Installation von Jaeger läuft mit einer einfachen Installation auf Basis eines Containers in einer Podman Runtime.

```bash
podman run -d --name jaeger \
  -e COLLECTOR_ZIPKIN_HOST_PORT=:9411 \
  -p 6831:6831/udp \
  -p 6832:6832/udp \
  -p 5778:5778 \
  -p 16686:16686 \
  -p 4317:4317 \
  -p 4318:4318 \
  -p 14250:14250 \
  -p 14268:14268 \
  -p 14269:14269 \
  -p 9411:9411 \
  jaegertracing/all-in-one:latest
```

Mit der URL http://localhost:16686/ kann dann die Console von Jeager im Webbrowser geöffnet werden.



### IP-Adresse des Jaeger-Containers ermitteln

Damit später die Kommunikation zwischen den Containern funktionieren kann, muss die IP-Adresse des Jaeger-Containers ermitteln werden. Diese IP-Adresse wird dann später in den Quarkus-Projekten benötigt:

```bash
podman container inspect -f '{{.NetworkSettings.IPAddress}}' jaeger
```

=> Ergebnis: 10.88.0.3



### tracing-test-app konfigurieren

In der Datei src/main/resources/application.properties den folgenden Abschnitt prüfen und ggf. die IP-Adresse aus dem Schritt zuvor eintragen:

```
quarkus.application.name=tracing-test-app 
quarkus.otel.exporter.otlp.endpoint=http://10.88.0.3:4317 
quarkus.otel.exporter.otlp.metrics.endpoint=http://10.88.0.3:4317 
```



## Anwendungsfall ausprobieren

1. Applikation tracing-test-app via CLI starten

```bash
mvn quarkus:dev
```

2. Befehl absetzen

```bash
curl -X POST http://localhost:8080/transx/start?id=4714 -H "Content-Type: application/json"  -d '{"id": "4714", "name":"Leo","shortName":"Leo", "keywords": [    "Alter=26",    "Geschlecht=männlich"   ]}'
```



<u>Applikation prüfen</u>

- DevUI aufrufen: http://localhost:8080/q/dev-ui/extensions
- Service A testen:

```bash
curl -X POST http://localhost:8080/ServiceA/process?id=4711 -H "Content-Type: application/json" -d '{"id": "4711", "name":"Leo","shortName":"Leo"}'
```

- Service B testen:

```
curl -X POST http://localhost:8080/ServiceB/process?id=4711 -H "Content-Type: application/json" -d '{"id": "4711", "name":"Leo","shortName":"Leo"}'
```



## Hinweise

Die Applikation nutzt zunächst einmal die Standardeinstellungen für Tracing. Ferner wurden folgende Customizations für das Distributed Tracing eingerichtet:

- OtelCustomInterceptor um den HTTP-Request auswerten zu können
- BusinessTransaction - in den aktuellen Span wird die Request und Response-Payload des REST-Services geschrieben -> könnte als Functional Interface aufgebaut werden.
- CamelController - Anlegen eines Custom Spans



## Ressources

- [Introducing native support for OpenTelemetry in Jaeger](https://medium.com/jaegertracing/introducing-native-support-for-opentelemetry-in-jaeger-eb661be8183c)  
  Blogeintrag beschreibt die Installationsvariante "all-in-one" auf Basis eines Containers. Gut zum Ausprobieren geeignet und Basis dieser Anleitung.
- [Getting Started with Jaeger](https://www.jaegertracing.io/docs/1.60/getting-started/)