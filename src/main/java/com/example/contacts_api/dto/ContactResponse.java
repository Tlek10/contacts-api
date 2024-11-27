package com.example.contacts_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ContactResponse {
    @Id
    private String id;
    private String name;
    private String phoneNumber;
    private String email;
    private String description;
    private Instant createdAt = Instant.now();


}
