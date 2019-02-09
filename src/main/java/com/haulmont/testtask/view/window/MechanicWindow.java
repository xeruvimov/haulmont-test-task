package com.haulmont.testtask.view.window;

import com.haulmont.testtask.data.dao.MechanicDAO;
import com.haulmont.testtask.data.entity.Mechanic;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;

public class MechanicWindow extends AbstractWindow {
    private static final int MAX_LENGTH_NAME = 50;
    private static final int MAX_LENGTH_TAXES = 9;

    private Mechanic mechanic;
    private MechanicDAO mechanicDAO;
    private Grid grid;

    private TextField firstNameField = new TextField("First Name");
    private TextField secondNameField = new TextField("Second Name");
    private TextField patronymicField = new TextField("Patronymic");
    private TextField taxesField = new TextField("Taxes");


    public MechanicWindow(MechanicDAO mechanicDAO, Grid grid) {
        this.mechanicDAO = mechanicDAO;
        this.grid = grid;

        initAllComponents();
    }

    public MechanicWindow(Mechanic mechanic, MechanicDAO mechanicDAO, Grid grid) {
        this.mechanic = mechanic;
        this.mechanicDAO = mechanicDAO;
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
            this.taxesField.validate();
            if (!this.taxesField.isValid()) {
                Notification.show("Number is invalid");
                return;
            }

            updateMechanic();
            refreshGrid();
            this.close();
        });
    }

    private void initFields() {
        configFields();
        if (this.mechanic != null) {
            this.firstNameField.setValue(this.mechanic.getFirstName());
            this.secondNameField.setValue(this.mechanic.getSecondName());
            this.patronymicField.setValue(this.mechanic.getPatronymic());
            this.taxesField.setValue(String.valueOf(this.mechanic.getTaxes()));
        } else {
            this.mechanic = new Mechanic();
        }
    }

    private void configMainLayout() {
        configLayout();

        this.main.addComponent(this.firstNameField);
        this.main.addComponent(this.secondNameField);
        this.main.addComponent(this.patronymicField);
        this.main.addComponent(this.taxesField);
        this.main.addComponent(this.buttons);

        setContent(this.main);
    }

    private void configFields() {
        this.firstNameField.setMaxLength(MAX_LENGTH_NAME);
        this.secondNameField.setMaxLength(MAX_LENGTH_NAME);
        this.patronymicField.setMaxLength(MAX_LENGTH_NAME);
        this.taxesField.setMaxLength(MAX_LENGTH_TAXES);
        this.taxesField.addValidator(new RegexpValidator("^[1-9]+[0-9]*$", "Taxes invalid"));
    }

    private void initCancelButton() {
        this.cancelButton.addClickListener((Button.ClickListener) clickEvent -> this.close());
    }

    private void refreshGrid() {
        this.grid.setContainerDataSource(
                new BeanItemContainer<>(Mechanic.class, this.mechanicDAO.getAll()));
    }

    private void updateMechanic() {
        this.mechanic.setFirstName(this.firstNameField.getValue());
        this.mechanic.setSecondName(this.secondNameField.getValue());
        this.mechanic.setPatronymic(this.patronymicField.getValue());
        this.mechanic.setTaxes(Integer.valueOf(this.taxesField.getValue()));

        this.mechanicDAO.saveOrUpdate(this.mechanic);
    }
}
