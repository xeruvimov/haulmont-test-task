package com.haulmont.testtask.view;

import com.haulmont.testtask.data.dao.ClientDAO;
import com.haulmont.testtask.data.dao.MechanicDAO;
import com.haulmont.testtask.data.dao.OrderDAO;
import com.haulmont.testtask.data.entity.Client;
import com.haulmont.testtask.data.entity.Mechanic;
import com.haulmont.testtask.data.entity.Order;
import com.haulmont.testtask.data.entity.Status;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;

import java.sql.Date;
import java.util.Arrays;

class OrderChangeWindow extends Window {
    private static final int MAX_LENGTH_PRICE = 9;
    private static final int MAX_LENGTH_DESCRIPTION = 500;

    private static final String WIDTH_FIELD = "400px";

    private Order order;
    private OrderDAO orderDAO;
    private ClientDAO clientDAO;
    private MechanicDAO mechanicDAO;
    private Grid grid;

    private ComboBox listClient = new ComboBox("Client");
    private ComboBox listMechanic = new ComboBox("Mechanic");
    private DateField startDate = new DateField("Start date");
    private DateField endDate = new DateField("End date");
    private TextField priceField = new TextField("Price");
    private NativeSelect statusSelect = new NativeSelect("Status");
    private TextArea descriptionField = new TextArea("Description");

    private Button okButton = new Button("OK");
    private Button cancelButton = new Button("Cancel");

    public OrderChangeWindow(Order order, OrderDAO orderDAO, ClientDAO clientDAO, MechanicDAO mechanicDAO, Grid grid) {
        this.order = order;
        this.orderDAO = orderDAO;
        this.clientDAO = clientDAO;
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

        main.addComponent(this.listClient);
        main.addComponent(this.listMechanic);
        main.addComponent(this.startDate);
        main.addComponent(this.endDate);
        main.addComponent(this.priceField);
        main.addComponent(this.statusSelect);
        main.addComponent(this.descriptionField);
        main.addComponent(buttons);

        center();
        setModal(true);
        setClosable(false);
        setResizable(false);

        setWidth("500px");

        setContent(main);
    }

    private void initFields() {
        this.listClient.setContainerDataSource(
                new BeanItemContainer<>(Client.class, this.clientDAO.getAll()));
        this.listClient.setNullSelectionAllowed(false);
        this.listClient.setValue(this.order.getClient());
        this.listClient.setFilteringMode(FilteringMode.CONTAINS);
        this.listClient.setWidth(WIDTH_FIELD);

        this.listMechanic.setContainerDataSource(
                new BeanItemContainer<>(Mechanic.class, this.mechanicDAO.getAll()));
        this.listMechanic.setNullSelectionAllowed(false);
        this.listMechanic.setValue(this.order.getMechanic());
        this.listMechanic.setFilteringMode(FilteringMode.CONTAINS);
        this.listMechanic.setWidth(WIDTH_FIELD);

        this.startDate.setValue(this.order.getStartDate());
        this.startDate.setResolution(Resolution.DAY);
        this.startDate.setWidth(WIDTH_FIELD);

        this.endDate.setValue(this.order.getEndDate());
        this.endDate.setResolution(Resolution.DAY);
        this.endDate.setWidth(WIDTH_FIELD);

        this.priceField.setValue(String.valueOf(this.order.getPrice()));
        this.priceField.setMaxLength(MAX_LENGTH_PRICE);
        this.priceField.addValidator(new RegexpValidator("^[1-9]+[0-9]*$", "Taxes invalid"));
        this.priceField.setWidth(WIDTH_FIELD);

        this.statusSelect.setContainerDataSource(
                new BeanItemContainer<>(Status.class, Arrays.asList(Status.values())));
        this.statusSelect.setValue(this.order.getStatus());
        this.statusSelect.setWidth(WIDTH_FIELD);

        this.descriptionField.setValue(this.order.getDescription());
        this.descriptionField.setMaxLength(MAX_LENGTH_DESCRIPTION);
        this.descriptionField.setWidth(WIDTH_FIELD);
    }

    private void initButtons() {
        initOkButton();
        initCancelButton();
    }

    private void initOkButton() {
        this.okButton.addClickListener((Button.ClickListener) clickEvent -> {
            this.priceField.validate();
            if (!this.priceField.isValid()) {
                Notification.show("Price is invalid");
                return;
            }

            if (this.startDate.getValue().after(this.endDate.getValue()) ||
                    this.startDate.getValue().equals(this.endDate.getValue())) {
                Notification.show("Date is invalid", Notification.Type.ERROR_MESSAGE);
                return;
            }

            this.order.setClient((Client) this.listClient.getValue());
            this.order.setMechanic((Mechanic) this.listMechanic.getValue());
            this.order.setStartDate(new Date(this.startDate.getValue().getTime()));
            this.order.setEndDate(new Date(this.endDate.getValue().getTime()));
            this.order.setDescription(this.descriptionField.getValue());
            this.order.setPrice(Integer.valueOf(this.priceField.getValue()));
            this.order.setStatus((Status) this.statusSelect.getValue());

            this.orderDAO.update(this.order);
            refreshGrid();
            this.close();
        });
    }

    private void initCancelButton() {
        this.cancelButton.addClickListener((Button.ClickListener) clickEvent -> this.close());
    }

    private void refreshGrid() {
        this.grid.setContainerDataSource(
                new BeanItemContainer<>(Order.class, this.orderDAO.getAll()));
    }
}
