package com.msa.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("service_configurations")
public class ServiceConfiguration {
    private String serviceName;
    private ArrayList<Property> applicationProperties; // spring.data.redis.port: {redis.ip}

    public String toString() {
        return String.join(" ", applicationProperties.stream().map(Property::toString).toList());
    }
}
