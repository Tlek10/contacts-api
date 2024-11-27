package com.example.contacts_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ContactRequest {
    private String name;
    private String phoneNumber;
    private String email;
    private String description;

}
