package com.msa.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricResult {
    private Map<String, String> metric;
    private List<Object> value;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MetricResult {");
        sb.append("metric=").append(metric);
        sb.append(", value=").append(value);
        sb.append("}");
        return sb.toString();
    }

}