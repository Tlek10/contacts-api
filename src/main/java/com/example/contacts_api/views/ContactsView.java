package com.example.contacts_api.views;

import com.example.contacts_api.dto.ContactRequest;
import com.example.contacts_api.dto.ContactResponse;
import com.example.contacts_api.service.ContactsService;
import com.example.contacts_api.utils.WordFileGenerator;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route("")
public class ContactsView extends VerticalLayout {

    private final ContactsService contactsService;

    private final Grid<ContactResponse> grid = new Grid<>(ContactResponse.class);
    private final List<ContactResponse> contacts = new ArrayList<>();

    public ContactsView(ContactsService contactsService) {
        this.contactsService = contactsService;

        setMargin(true);
        setPadding(true);
        setSpacing(true);

        configureGrid();

        Button addContactButton = new Button("Add Contact", e -> openEditorDialog(null));
        Button printAllButton = new Button("Print All", e -> {
            List<ContactResponse> contacts = contactsService.getAllContacts();
            generateWordFile(contacts);
        });

        add(new HorizontalLayout(addContactButton, printAllButton), grid);

        updateGrid();
    }

    private void configureGrid() {
        grid.setColumns("name", "phoneNumber", "email", "description");

        grid.getColumnByKey("name").setHeader("Name");
        grid.getColumnByKey("email").setHeader("Email");
        grid.getColumnByKey("phoneNumber").setHeader("Phone Number");
        grid.getColumnByKey("description").setHeader("Description");

        grid.addComponentColumn(contact -> {
            Button editButton = new Button("Edit", e -> openEditorDialog(contact));
            Button deleteButton = new Button("Delete", e -> deleteContact(contact));
            Button downloadButton = new Button("Download", e -> generateContactWordFile(contact));

            deleteButton.getStyle().set("color", "red");
            downloadButton.getStyle().set("color", "blue");

            VerticalLayout buttonsLayout = new VerticalLayout(editButton, deleteButton, downloadButton);
            buttonsLayout.setSpacing(false);
            return buttonsLayout;
        }).setHeader("Actions");


        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);

        List<ContactResponse> contacts = contactsService.getAllContacts();
        grid.setItems(contacts);

        add(grid);
    }

    private void openEditorDialog(ContactResponse contact) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(contact == null ? "Create Contact" : "Edit Contact");

        TextField nameField = new TextField("Name");
        TextField phoneNumberField = new TextField("Phone Number");
        EmailField emailField = new EmailField("Email");
        TextField descriptionField = new TextField("Description");

        if (contact != null) {
            nameField.setValue(contact.getName());
            phoneNumberField.setValue(contact.getPhoneNumber());
            emailField.setValue(contact.getEmail());
            descriptionField.setValue(contact.getDescription());
        }

        FormLayout formLayout = new FormLayout(nameField, phoneNumberField, emailField, descriptionField);

        Button saveButton = new Button("Save", e -> {
            if (contact == null) {
                createContact(new ContactRequest(
                        nameField.getValue(),
                        phoneNumberField.getValue(),
                        emailField.getValue(),
                        descriptionField.getValue()
                ));
            } else {
                updateContact(contact.getId(), new ContactRequest(
                        nameField.getValue(),
                        phoneNumberField.getValue(),
                        emailField.getValue(),
                        descriptionField.getValue()
                ));
            }
            dialog.close();
        });

        Button cancelButton = new Button("Cancel", e -> dialog.close());
        cancelButton.getStyle().set("color", "red");

        dialog.add(formLayout, new VerticalLayout(saveButton, cancelButton));
        dialog.open();
    }

    private void updateGrid() {
        contacts.clear();
        contacts.addAll(contactsService.getAllContacts());
        grid.setItems(contacts);
    }

    private void createContact(ContactRequest contactRequest) {
        contactsService.createContact(contactRequest);
        Notification.show("Contact created successfully");
        updateGrid();
    }

    private void updateContact(String id, ContactRequest contactRequest) {
        contactsService.updateContact(id, contactRequest);
        Notification.show("Contact updated successfully");
        updateGrid();
    }

    private void deleteContact(ContactResponse contact) {
        contactsService.deleteContactById(contact.getId());
        Notification.show("Contact deleted successfully");
        updateGrid();
    }

    private void generateWordFile(List<ContactResponse> contacts) {
        List<String> content = new ArrayList<>();
        content.add("Contacts List");

        for (ContactResponse contact : contacts) {
            content.add(
                    "ID: " + contact.getId() +
                            "\nName: " + contact.getName() +
                            "\nPhone: " + contact.getPhoneNumber() +
                            "\nEmail: " + contact.getEmail() +
                            "\nDescription: " + contact.getDescription()
            );
            content.add(""); // Добавляем пустую строку между контактами
        }

        WordFileGenerator.saveToWordFile(content, "contacts.docx");
    }
    private void generateContactWordFile(ContactResponse contact) {
        List<String> content = List.of(
                "Contact Details",
                "ID: " + contact.getId(),
                "Name: " + contact.getName(),
                "Phone: " + contact.getPhoneNumber(),
                "Email: " + contact.getEmail(),
                "Description: " + contact.getDescription(),
                "Created At: " + contact.getCreatedAt()
        );
        WordFileGenerator.saveToWordFile(content, "contact_" + contact.getId() + ".docx");
    }

}
