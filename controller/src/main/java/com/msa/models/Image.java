package com.msa.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("images")
public class Image {
    @Id
    private String id;
    private String imageName;
    private String script;
    private HashMap<String, String> placeholders = new HashMap<>();
}
