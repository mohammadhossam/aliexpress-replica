package com.msa.models.prometheus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Global {
    private String scrape_interval;
    private String evaluation_interval;
    private String scrape_timeout;
}
