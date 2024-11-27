package com.example.contacts_api.service;


import com.example.contacts_api.dto.ContactRequest;
import com.example.contacts_api.dto.ContactResponse;
import com.example.contacts_api.exceptions.PhoneNumberAlreadyExistsException;
import com.example.contacts_api.model.Contact;
import com.example.contacts_api.repository.ContactsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContactsService {

    private final ContactsRepository contactsRepository;

    public List<ContactResponse> getAllContacts() {
        List<Contact> contacts = contactsRepository.findAll();

        return contacts.stream().map(this::mapToContactsResponse).toList();
    }

    private ContactResponse mapToContactsResponse(Contact contact) {
        return ContactResponse.builder()
                .id(contact.getId())
                .name(contact.getName())
                .phoneNumber(contact.getPhoneNumber())
                .description(contact.getDescription())
                .email(contact.getEmail())
                .createdAt(contact.getCreatedAt())
                .build();
    }

    public void createContact(ContactRequest contactRequest) {
        checkIfPhoneNumberExists(contactRequest.getPhoneNumber(), null);

        Contact contact = Contact.builder()
                .name(contactRequest.getName())
                .email(contactRequest.getEmail())
                .phoneNumber(contactRequest.getPhoneNumber())
                .description(contactRequest.getDescription())
                .build();

        contactsRepository.save(contact);
        log.info("Contact {} is saved", contact.getId());
    }

    public void updateContact(String id, ContactRequest contactRequest) {
        Contact contact = findContactById(id);

        checkIfPhoneNumberExists(contactRequest.getPhoneNumber(), id);

        contact.setName(contactRequest.getName());
        contact.setPhoneNumber(contactRequest.getPhoneNumber());
        contact.setEmail(contactRequest.getEmail());
        contact.setDescription(contactRequest.getDescription());

        contactsRepository.save(contact);
        log.info("Contact {} is updated", id);
    }

    public void deleteContactById(String id) {
        Contact contact = findContactById(id);
        contactsRepository.delete(contact);
        log.info("Contact {} is deleted", id);
    }

    private Contact findContactById(String id) {
        return contactsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));
    }

    private void checkIfPhoneNumberExists(String phoneNumber, String contactId) {
        Optional<Contact> existingContact = contactsRepository.findByPhoneNumber(phoneNumber);

        if (existingContact.isPresent() && !existingContact.get().getId().equals(contactId)) {
            throw new PhoneNumberAlreadyExistsException("Phone number " + phoneNumber + " already exists");
        }
    }

}