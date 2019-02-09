package com.haulmont.testtask.view.window;

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

public class OrderWindow extends AbstractWindow {
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


    public OrderWindow(OrderDAO orderDAO, ClientDAO clientDAO, MechanicDAO mechanicDAO, Grid grid) {
        this.orderDAO = orderDAO;
        this.clientDAO = clientDAO;
        this.mechanicDAO = mechanicDAO;
        this.grid = grid;

        initAllComponents();
    }

    public OrderWindow(Order order, OrderDAO orderDAO, ClientDAO clientDAO, MechanicDAO mechanicDAO, Grid grid) {
        this.order = order;
        this.orderDAO = orderDAO;
        this.clientDAO = clientDAO;
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

            updateOrder();
            refreshGrid();
            this.close();
        });
    }

    private void configMainLayout() {
        configLayout();

        this.main.addComponent(this.listClient);
        this.main.addComponent(this.listMechanic);
        this.main.addComponent(this.startDate);
        this.main.addComponent(this.endDate);
        this.main.addComponent(this.priceField);
        this.main.addComponent(this.statusSelect);
        this.main.addComponent(this.descriptionField);
        this.main.addComponent(this.buttons);

        setWidth("450px");
        setContent(main);
    }

    private void updateOrder() {
        this.order.setClient((Client) this.listClient.getValue());
        this.order.setMechanic((Mechanic) this.listMechanic.getValue());
        this.order.setStartDate(new Date(this.startDate.getValue().getTime()));
        this.order.setEndDate(new Date(this.endDate.getValue().getTime()));
        this.order.setDescription(this.descriptionField.getValue());
        this.order.setPrice(Integer.valueOf(this.priceField.getValue()));
        this.order.setStatus((Status) this.statusSelect.getValue());

        this.orderDAO.saveOrUpdate(this.order);
    }

    private void initFields() {
        configFields();

        if (this.order != null) {
            this.listClient.setValue(this.order.getClient()); //не работает, я устал и хз что с этим делать
            this.listMechanic.setValue(this.order.getMechanic());
            this.startDate.setValue(this.order.getStartDate());
            this.endDate.setValue(this.order.getEndDate());
            this.priceField.setValue(String.valueOf(this.order.getPrice()));
            this.statusSelect.setValue(this.order.getStatus());
            this.descriptionField.setValue(this.order.getDescription());
        } else {
            this.order = new Order();
        }
    }

    private void configFields() {
        this.listClient.setNullSelectionAllowed(false);
        this.listClient.setFilteringMode(FilteringMode.CONTAINS);
        this.listClient.setWidth(WIDTH_FIELD);

        this.listMechanic.setNullSelectionAllowed(false);
        this.listMechanic.setFilteringMode(FilteringMode.CONTAINS);
        this.listMechanic.setWidth(WIDTH_FIELD);

        this.startDate.setResolution(Resolution.DAY);
        this.startDate.setWidth(WIDTH_FIELD);

        this.endDate.setResolution(Resolution.DAY);
        this.endDate.setWidth(WIDTH_FIELD);

        this.priceField.setMaxLength(MAX_LENGTH_PRICE);
        this.priceField.addValidator(new RegexpValidator("^[1-9]+[0-9]*$", "Taxes invalid"));
        this.priceField.setWidth(WIDTH_FIELD);

        this.statusSelect.setNullSelectionAllowed(false);
        this.statusSelect.setWidth(WIDTH_FIELD);

        this.descriptionField.setMaxLength(MAX_LENGTH_DESCRIPTION);
        this.descriptionField.setWidth(WIDTH_FIELD);

        this.listClient.setContainerDataSource(
                new BeanItemContainer<>(Client.class, this.clientDAO.getAll()));
        this.listMechanic.setContainerDataSource(
                new BeanItemContainer<>(Mechanic.class, this.mechanicDAO.getAll()));
        this.statusSelect.setContainerDataSource(
                new BeanItemContainer<>(Status.class, Arrays.asList(Status.values())));
    }

    private void initCancelButton() {
        this.cancelButton.addClickListener((Button.ClickListener) clickEvent -> this.close());
    }

    private void refreshGrid() {
        this.grid.setContainerDataSource(
                new BeanItemContainer<>(Order.class, this.orderDAO.getAll()));
    }
}
