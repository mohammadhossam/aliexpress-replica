package com.msa.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("running_resource")
public class RunningResource {
    @Id
    private String id;
    private String resourceName;
    private HashMap<String, String> attributes = new HashMap<>();
}
