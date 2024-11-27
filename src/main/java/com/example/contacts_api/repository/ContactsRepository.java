package com.example.contacts_api.repository;

import com.example.contacts_api.model.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ContactsRepository extends MongoRepository<Contact, String> {

    Optional<Contact> findByPhoneNumber(String phoneNumber);
}
