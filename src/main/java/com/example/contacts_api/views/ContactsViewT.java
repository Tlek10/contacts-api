package com.example.contacts_api.views;

import com.example.contacts_api.dto.ContactRequest;
import com.example.contacts_api.dto.ContactResponse;
import com.example.contacts_api.service.ContactsService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route("")
public class ContactsViewT extends VerticalLayout {

    private final ContactsService contactsService;

    private final Grid<ContactResponse> grid = new Grid<>(ContactResponse.class);
    private final List<ContactResponse> contacts = new ArrayList<>();

    public ContactsViewT(ContactsService contactsService) {
        this.contactsService = contactsService;

        setMargin(true);
        setPadding(true);
        setSpacing(true);

        configureGrid();

        Button addContactButton = new Button("Add Contact", e -> openEditorDialog(null));

        add(addContactButton, grid);

        updateGrid();
    }

    private void configureGrid() {
        grid.setColumns("id", "name", "phoneNumber", "email", "description", "createdAt");

        grid.getColumnByKey("id").setHeader("ID");
        grid.getColumnByKey("name").setHeader("Name");
        grid.getColumnByKey("phoneNumber").setHeader("Phone Number");
        grid.getColumnByKey("email").setHeader("Email");
        grid.getColumnByKey("description").setHeader("Description");
        grid.getColumnByKey("createdAt").setHeader("Created At");

        grid.addComponentColumn(contact -> {
            Button editButton = new Button("Edit", e -> openEditorDialog(contact));
            Button deleteButton = new Button("Delete", e -> deleteContact(contact));
            deleteButton.getStyle().set("color", "red");

            VerticalLayout buttonsLayout = new VerticalLayout(editButton, deleteButton);
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
}
