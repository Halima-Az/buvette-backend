package com.buvette.buvette_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

@Data
@Document
public class Role {
    @Id
    private String id;
    private String name;
}
