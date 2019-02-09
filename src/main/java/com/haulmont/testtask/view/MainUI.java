package com.haulmont.testtask.view;

import com.haulmont.testtask.data.DAOFactory;
import com.haulmont.testtask.data.dao.ClientDAO;
import com.haulmont.testtask.data.dao.MechanicDAO;
import com.haulmont.testtask.data.dao.OrderDAO;
import com.haulmont.testtask.data.entity.Client;
import com.haulmont.testtask.data.entity.Mechanic;
import com.haulmont.testtask.data.entity.Order;
import com.haulmont.testtask.view.window.ClientWindow;
import com.haulmont.testtask.view.window.MechanicWindow;
import com.haulmont.testtask.view.window.OrderWindow;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Collection;
import java.util.Objects;

@Theme(value = ValoTheme.THEME_NAME)
public class MainUI extends UI {
    private HorizontalLayout mainLayout = new HorizontalLayout();

    private Button buttonClient = new Button("Clients");
    private Button buttonMechanic = new Button("Mechanics");
    private Button buttonOrder = new Button("Orders");
    private String leftButtonWidth = "200px";
    private String leftMenuWidth = "400px";

    private Button buttonAdd = new Button("Add new object");
    private Button buttonChange = new Button("Change selected");
    private Button buttonDelete = new Button("Delete selected");

    private Label label = new Label("Test label");
    private Grid grid = new Grid();

    private ClientDAO clientDAO;
    private MechanicDAO mechanicDAO;
    private OrderDAO orderDAO;

    @Override
    protected void init(VaadinRequest request) {
        this.clientDAO = DAOFactory.getClientDAO();
        this.mechanicDAO = DAOFactory.getMechanicDAO();
        this.orderDAO = DAOFactory.getOrderDAO();

        this.mainLayout.setSpacing(true);
        this.mainLayout.setMargin(true);

        initLeftMenu();
        initRightMenu();

        setContent(mainLayout);
    }

    private void initRightMenu() {
        this.grid.setWidth("800px");
        this.grid.setHeight("700px");

        VerticalLayout rightData = new VerticalLayout();
        rightData.setSpacing(true);
        rightData.addComponent(this.label);
        rightData.addComponent(this.grid);

        HorizontalLayout controlButtons = new HorizontalLayout();
        controlButtons.setSpacing(true);

        controlButtons.addComponent(this.buttonAdd);
        controlButtons.addComponent(this.buttonChange);
        controlButtons.addComponent(this.buttonDelete);

        rightData.addComponent(controlButtons);

        this.mainLayout.addComponent(rightData);
    }

    private void initLeftMenu() {
        this.buttonClient.addClickListener((Button.ClickListener) clickEvent -> setRightMenu(Client.class));
        this.buttonMechanic.addClickListener((Button.ClickListener) clickEvent -> setRightMenu(Mechanic.class));
        this.buttonOrder.addClickListener((Button.ClickListener) clickEvent -> setRightMenu(Order.class));

        this.buttonClient.setWidth(this.leftButtonWidth);
        this.buttonMechanic.setWidth(this.leftButtonWidth);
        this.buttonOrder.setWidth(this.leftButtonWidth);

        VerticalLayout leftMenu = new VerticalLayout();
        leftMenu.setSpacing(true);

        leftMenu.addComponent(this.buttonClient);
        leftMenu.addComponent(this.buttonMechanic);
        leftMenu.addComponent(this.buttonOrder);
        leftMenu.setWidth(this.leftMenuWidth);

        this.mainLayout.addComponent(leftMenu);
    }

    private void setRightMenu(Class item) {
        setLabel(item);
        setGrid(item);
        setControlButtonListeners(item);
    }

    private void setLabel(Class item) {
        if (item.equals(Client.class)) {
            this.label.setValue("Clients");
        } else if (item.equals(Mechanic.class)) {
            this.label.setValue("Mechanics");
        } else if (item.equals(Order.class)) {
            this.label.setValue("Orders");
        }
    }

    private void setGrid(Class item) {
        Collection collection;
        BeanItemContainer container = null;

        if (item.equals(Client.class)) {
            collection = this.clientDAO.getAll();
            container = new BeanItemContainer<Client>(Client.class, collection);
        } else if (item.equals(Mechanic.class)) {
            collection = this.mechanicDAO.getAll();
            container = new BeanItemContainer<Mechanic>(Mechanic.class, collection);
        } else if (item.equals(Order.class)) {
            collection = this.orderDAO.getAll();
            container = new BeanItemContainer<Order>(Order.class, collection);
        }
        this.grid.removeAllColumns();
        this.grid.setContainerDataSource(Objects.requireNonNull(container));
    }

    private void setControlButtonListeners(Class item) {
        setDeleteButton(item);
        setChangeButton(item);
        setAddButton(item);
    }

    private void setDeleteButton(Class item) {
        this.buttonDelete.getListeners(Button.ClickEvent.class)
                .forEach(listener -> this.buttonDelete.removeListener(Button.ClickEvent.class, listener));

        this.buttonDelete.addClickListener((Button.ClickListener) (clickEvent) -> {
            Object selected = ((Grid.SingleSelectionModel)
                    this.grid.getSelectionModel()).getSelectedRow();
            if (selected != null) {
                Object check = null;
                if (item.equals(Client.class)) {
                    this.clientDAO.delete((Client) selected);
                    check = this.clientDAO.findByID(((Client) selected).getId());
                } else if (item.equals(Mechanic.class)) {
                    this.mechanicDAO.delete((Mechanic) selected);
                    check = this.mechanicDAO.findByID(((Mechanic) selected).getId());
                } else if (item.equals(Order.class)) {
                    this.orderDAO.delete((Order) selected);
                    check = this.orderDAO.findByID(((Order) selected).getId());
                }

                if (check != null) {
                    Notification.show("This client or mechanic have active order",
                            Notification.Type.ERROR_MESSAGE);
                } else {
                    this.grid.getContainerDataSource().removeItem(selected);
                    this.grid.getSelectionModel().reset();
                }
            }
        });
    }

    private void setChangeButton(Class item) {
        this.buttonChange.getListeners(Button.ClickEvent.class)
                .forEach(listener -> this.buttonChange.removeListener(Button.ClickEvent.class, listener));
        this.buttonChange.addClickListener((Button.ClickListener) (clickEvent) -> {
            Object selected = ((Grid.SingleSelectionModel)
                    this.grid.getSelectionModel()).getSelectedRow();

            if (selected != null) {
                if (item.equals(Client.class)) {
                    addWindow(new ClientWindow((Client) selected, this.clientDAO, this.grid));
                } else if (item.equals(Mechanic.class)) {
                    addWindow(new MechanicWindow((Mechanic) selected, this.mechanicDAO, this.grid));
                } else if (item.equals(Order.class)) {
                    addWindow(new OrderWindow((Order) selected,
                            this.orderDAO, this.clientDAO, this.mechanicDAO, this.grid));
                }
            }
        });
    }

    private void setAddButton(Class item) {
        this.buttonAdd.getListeners(Button.ClickEvent.class)
                .forEach(listener -> this.buttonAdd.removeListener(Button.ClickEvent.class, listener));
        this.buttonAdd.addClickListener((Button.ClickListener) (clickEvent) -> {
            if (item.equals(Client.class)) {
                addWindow(new ClientWindow(this.clientDAO, this.grid));
            } else if (item.equals(Mechanic.class)) {
                addWindow(new MechanicWindow(this.mechanicDAO, this.grid));
            } else if (item.equals(Order.class)) {
                addWindow(new OrderWindow(this.orderDAO, this.clientDAO, this.mechanicDAO, this.grid));
            }
        });
    }
}