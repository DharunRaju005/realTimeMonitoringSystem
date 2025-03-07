package org.example.config;


import io.prometheus.client.exporter.HTTPServer;
import org.springframework.context.annotation.Configuration;
import io.prometheus.client.hotspot.DefaultExports;
import java.io.IOException;
import jakarta.annotation.PostConstruct;

@Configuration


public class PrometheusConfig {
    @PostConstruct
    public void init() throws IOException {
        DefaultExports.initialize();
        HTTPServer server=new HTTPServer("0.0.0.0",8082); //this shd match  the port prometheus will scraping

    }
}
