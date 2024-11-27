package com.example.contacts_api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;


@Document(value = "contact")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Contact {

    @Id
    private String id;
    private String name;
    @Indexed(unique = true)
    private String phoneNumber;
    private String email;
    private String description;
    private Instant createdAt = Instant.now();

}
