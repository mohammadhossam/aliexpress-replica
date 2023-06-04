package com.msa.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Property {
    private String propertyName; // spring.data.redis.port
    private String val; // should contain placeholders for the values of external resources for example: {users-service-cache.port}

    public String toString() {
        return "-D" + propertyName + "=" + val;
    }
}
