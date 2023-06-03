package com.msa.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Document("machines")
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String ip;
    private int memory;
    private String username;
}
