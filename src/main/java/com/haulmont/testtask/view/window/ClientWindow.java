package com.haulmont.testtask.view.window;

import com.haulmont.testtask.data.dao.ClientDAO;
import com.haulmont.testtask.data.entity.Client;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;

public class ClientWindow extends AbstractWindow {
    private static final int MAX_LENGTH_NAME = 50;
    private static final int MAX_LENGTH_NUMBER = 11;

    private Client client;
    private ClientDAO clientDAO;
    private Grid grid;

    private TextField firstNameField = new TextField("First Name");
    private TextField secondNameField = new TextField("Second Name");
    private TextField patronymicField = new TextField("Patronymic");
    private TextField numberField = new TextField("Number");

    public ClientWindow(ClientDAO clientDAO, Grid grid) {
        this.clientDAO = clientDAO;
        this.grid = grid;

        initAllComponents();
    }

    public ClientWindow(Client client, ClientDAO clientDAO, Grid grid) {
        this.client = client;
        this.clientDAO = clientDAO;
        this.grid = grid;

        initAllComponents();
    }

    private void initAllComponents() {
        initFields();
        initButtons();
        configMainLayout();
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

            updateClient();
            refreshGrid();
            this.close();
        });
    }

    private void configMainLayout() {
        configLayout();

        this.main.addComponent(this.firstNameField);
        this.main.addComponent(this.secondNameField);
        this.main.addComponent(this.patronymicField);
        this.main.addComponent(this.numberField);
        this.main.addComponent(this.buttons);

        setContent(this.main);
    }

    private void initCancelButton() {
        this.cancelButton.addClickListener((Button.ClickListener) clickEvent -> this.close());
    }

    private void refreshGrid() {
        this.grid.setContainerDataSource(
                new BeanItemContainer<>(Client.class, this.clientDAO.getAll()));
    }

    private void initFields() {
        configFields();
        if (this.client != null) {
            this.firstNameField.setValue(this.client.getFirstName());
            this.secondNameField.setValue(this.client.getSecondName());
            this.patronymicField.setValue(this.client.getPatronymic());
            this.numberField.setValue(this.client.getNumber());
        } else {
            this.client = new Client();
        }
    }

    private void configFields() {
        this.firstNameField.setMaxLength(MAX_LENGTH_NAME);
        this.secondNameField.setMaxLength(MAX_LENGTH_NAME);
        this.patronymicField.setMaxLength(MAX_LENGTH_NAME);
        this.numberField.setMaxLength(MAX_LENGTH_NUMBER);
        this.numberField.addValidator(new RegexpValidator("^[1-9]+[0-9]*$", "Number invalid"));
    }

    private void updateClient() {
        this.client.setFirstName(this.firstNameField.getValue());
        this.client.setSecondName(this.secondNameField.getValue());
        this.client.setPatronymic(this.patronymicField.getValue());
        this.client.setNumber(this.numberField.getValue());

        this.clientDAO.saveOrUpdate(client);
    }
}
