package com.msa.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("running_instances")
public class RunningInstance {
    @Id
    private String id;
    private Machine host;
    private String port;
    private ServiceType serviceType;
}
