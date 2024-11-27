package com.example.contacts_api.controller;

import com.example.contacts_api.dto.ContactRequest;
import com.example.contacts_api.dto.ContactResponse;
import com.example.contacts_api.service.ContactsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/contact")
@AllArgsConstructor
public class ContactController {

    private final ContactsService contactsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ContactResponse> getAllContacts() {
        return contactsService.getAllContacts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createContact(@RequestBody ContactRequest contactRequest) {
        contactsService.createContact(contactRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateContact(@PathVariable String id,
                              @RequestBody ContactRequest contactRequest) {
        contactsService.updateContact(id, contactRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContact(@PathVariable String id) {
        contactsService.deleteContactById(id);
    }
}
