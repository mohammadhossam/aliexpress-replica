package com.msa.models.prometheus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScrapeConfig {
    private String job_name;
    private String metrics_path;
    List<StaticConfig> static_configs;
}
