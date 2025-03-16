# Real-Time Monitoring System

Demo Video: [Watch here](https://drive.google.com/drive/folders/1zOvgYa8uSubYnLk4JgQ7rak29E3_kIXn?usp=sharing)

## Overview
The **Real-Time Monitoring System** is a Spring Boot-based application that integrates with Apache Kafka for event-driven messaging. It exposes Kafka consumer metrics to Prometheus, which are then visualized using Grafana. The system monitors and tracks different types of events, such as urgent and normal clicks, in real-time.

## Architecture
- **Spring Boot Application**: Produces and consumes Kafka messages and exposes metrics.
- **Apache Kafka**: Handles real-time event streaming.
- **Prometheus**: Scrapes application metrics for monitoring.
- **Grafana**: Provides a visual representation of the metrics.
- **Oracle VirtualBox VM**: Hosts Kafka, Prometheus, and Grafana services.

## Features
- Real-time Kafka message processing.
- Metrics exposure for Prometheus via **Micrometer**.
- Grafana dashboards for data visualization.
- Tracks different event types like **urgent** and **normal** clicks.

## Prerequisites
Ensure you have the following installed:
- Java 17+
- Spring Boot
- Apache Kafka
- Prometheus
- Grafana
- Oracle VirtualBox (for running Kafka, Prometheus, and Grafana inside a VM)

## Setup & Configuration

### 1. Running Kafka on VM
Make sure Kafka is running inside your VM:
```bash
bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties
```

### 2. Configuring Spring Boot Application
Set up Kafka Consumer in **application.properties**:
```properties
spring.kafka.bootstrap-servers=<VM-IP>:9092
spring.kafka.consumer.group-id=mygroup
management.endpoints.web.exposure.include=*
management.prometheus.metrics.export.enabled=true
management.endpoint.prometheus.enabled=true
```

### 3. Configuring Prometheus
Modify `/etc/prometheus/prometheus.yml` to scrape Spring Boot metrics:
```yaml
scrape_configs:
  - job_name: 'spring-boot-app'
    static_configs:
      - targets: ['<VM-IP>:8082']
  - job_name: 'kafka'
    static_configs:
      - targets: ['<VM-IP>:8082']
```
Restart Prometheus:
```bash
systemctl restart prometheus
```

### 4. Running Grafana
Access Grafana inside the VM and configure Prometheus as a data source:
- Open Grafana at `http://<VM-IP>:3000`
- Add Prometheus (`http://localhost:9090`) as a data source
- Create dashboards for visualizing Kafka metrics

## Viewing Metrics in Prometheus
Visit:
```bash
http://<VM-IP>:8082/metrics
```
Example output:
```
# HELP kafka_urgent_events_total total urgent events consumed by kafka
# TYPE kafka_urgent_events_total counter
kafka_urgent_events_total 11.0

# HELP kafka_normal_events_total total normal events consumed by kafka
# TYPE kafka_normal_events_total counter
kafka_normal_events_total 4.0
```

## Viewing Metrics in Grafana
1. Open Grafana at `http://<VM-IP>:3000`
2. Select the Prometheus data source
3. Query metrics like `kafka_urgent_events_total` and `kafka_normal_events_total`
4. Visualize them in real-time using dashboards

## Conclusion
This project enables real-time monitoring of Kafka events using Prometheus and Grafana, providing valuable insights into system performance and event tracking.



