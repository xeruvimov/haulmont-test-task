package com.haulmont.testtask.view;

import com.haulmont.testtask.data.dao.ClientDAO;
import com.haulmont.testtask.data.entity.Client;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;

class ClientChangeWindow extends Window {
    private static final int MAX_LENGTH_NAME = 50;
    private static final int MAX_LENGTH_NUMBER = 11;

    private Client client;
    private ClientDAO clientDAO;
    private Grid grid;

    private TextField firstNameField = new TextField("First Name");
    private TextField secondNameField = new TextField("Second Name");
    private TextField patronymicField = new TextField("Patronymic");
    private TextField numberField = new TextField("Number");

    private Button okButton = new Button("OK");
    private Button cancelButton = new Button("Cancel");

    ClientChangeWindow(Client client, ClientDAO clientDAO, Grid grid) {
        this.client = client;
        this.clientDAO = clientDAO;
        this.grid = grid;

        initFields();
        initButtons();



        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.setMargin(true);

        buttons.addComponent(this.okButton);
        buttons.addComponent(this.cancelButton);

        VerticalLayout main = new VerticalLayout();
        main.setMargin(true);
        main.setSpacing(true);

        main.addComponent(this.firstNameField);
        main.addComponent(this.secondNameField);
        main.addComponent(this.patronymicField);
        main.addComponent(this.numberField);
        main.addComponent(buttons);

        center();
        setModal(true);
        setClosable(false);
        setResizable(false);

        setContent(main);
    }

    private void initFields() {
        this.firstNameField.setValue(this.client.getFirstName());
        this.firstNameField.setMaxLength(MAX_LENGTH_NAME);

        this.secondNameField.setValue(this.client.getSecondName());
        this.secondNameField.setMaxLength(MAX_LENGTH_NAME);

        this.patronymicField.setValue(this.client.getPatronymic());
        this.patronymicField.setMaxLength(MAX_LENGTH_NAME);

        this.numberField.setValue(this.client.getNumber());
        this.numberField.setMaxLength(MAX_LENGTH_NUMBER);
        this.numberField.addValidator(new RegexpValidator("^[1-9]+[0-9]*$", "Number invalid"));
    }

    private void initButtons() {
        initOkButton();
        initCancelButton();
    }

    private void initOkButton() {
        this.okButton.addClickListener((Button.ClickListener) clickEvent -> {
            this.numberField.validate();
            if (!this.numberField.isValid()) {
                Notification.show("Number invalid");
                return;
            }

            this.client.setFirstName(this.firstNameField.getValue());
            this.client.setSecondName(this.secondNameField.getValue());
            this.client.setPatronymic(this.patronymicField.getValue());
            this.client.setNumber(this.numberField.getValue());

            this.clientDAO.update(this.client);
            refreshGrid();
            this.close();
        });
    }

    private void initCancelButton() {
        this.cancelButton.addClickListener((Button.ClickListener) clickEvent -> this.close());
    }

    private void refreshGrid() {
        this.grid.setContainerDataSource(
                new BeanItemContainer<>(Client.class, this.clientDAO.getAll()));
    }
}
