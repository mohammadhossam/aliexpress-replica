package com.aliexpress.merchantauthenticationservice.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Resources {

    private final MeterRegistry registry;

    @Autowired
    public Resources(MeterRegistry registry) {
        this.registry = registry;
        createCustomMetrics();
    }

    private void createCustomMetrics() {
        Gauge.builder("app.memory.used", Runtime.getRuntime(), runtime -> (double) (runtime.totalMemory() - runtime.freeMemory()))
                .description("Total memory used by application")
                .baseUnit("bytes")
                .register(registry);
    }


}