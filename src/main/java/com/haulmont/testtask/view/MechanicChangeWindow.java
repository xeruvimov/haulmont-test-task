package com.haulmont.testtask.view;

import com.haulmont.testtask.data.dao.MechanicDAO;
import com.haulmont.testtask.data.entity.Mechanic;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;

class MechanicChangeWindow extends Window {
    private static final int MAX_LENGTH_NAME = 50;
    private static final int MAX_LENGTH_TAXES = 9;

    private Mechanic mechanic;
    private MechanicDAO mechanicDAO;
    private Grid grid;

    private TextField firstNameField = new TextField("First Name");
    private TextField secondNameField = new TextField("Second Name");
    private TextField patronymicField = new TextField("Patronymic");
    private TextField taxesField = new TextField("Taxes");

    private Button okButton = new Button("OK");
    private Button cancelButton = new Button("Cancel");


    public MechanicChangeWindow(Mechanic mechanic, MechanicDAO mechanicDAO, Grid grid) {
        this.mechanic = mechanic;
        this.mechanicDAO = mechanicDAO;
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
        main.addComponent(this.taxesField);
        main.addComponent(buttons);

        center();
        setModal(true);
        setClosable(false);
        setResizable(false);

        setContent(main);
    }

    private void initFields() {
        this.firstNameField.setValue(this.mechanic.getFirstName());
        this.firstNameField.setMaxLength(MAX_LENGTH_NAME);

        this.secondNameField.setValue(this.mechanic.getSecondName());
        this.secondNameField.setMaxLength(MAX_LENGTH_NAME);

        this.patronymicField.setValue(this.mechanic.getPatronymic());
        this.patronymicField.setMaxLength(MAX_LENGTH_NAME);

        this.taxesField.setValue(String.valueOf(this.mechanic.getTaxes()));
        this.taxesField.setMaxLength(MAX_LENGTH_TAXES);
        this.taxesField.addValidator(new RegexpValidator("^[1-9]+[0-9]*$", "Taxes invalid"));
    }

    private void initButtons() {
        initOkButton();
        initCancelButton();
    }
    private void initOkButton() {
        this.okButton.addClickListener((Button.ClickListener) clickEvent -> {
            this.taxesField.validate();
            if (!this.taxesField.isValid()) {
                Notification.show("Number is invalid");
                return;
            }

            this.mechanic.setFirstName(this.firstNameField.getValue());
            this.mechanic.setSecondName(this.secondNameField.getValue());
            this.mechanic.setPatronymic(this.patronymicField.getValue());
            this.mechanic.setTaxes(Integer.valueOf(this.taxesField.getValue()));

            this.mechanicDAO.update(this.mechanic);
            refreshGrid();
            this.close();
        });
    }

    private void initCancelButton() {
        this.cancelButton.addClickListener((Button.ClickListener) clickEvent -> this.close());
    }

    private void refreshGrid() {
        this.grid.setContainerDataSource(
                new BeanItemContainer<>(Mechanic.class, this.mechanicDAO.getAll()));
    }
}
