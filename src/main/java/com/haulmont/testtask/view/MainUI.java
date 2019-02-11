package com.haulmont.testtask.view;

import com.haulmont.testtask.data.DAOFactory;
import com.haulmont.testtask.data.dao.ClientDAO;
import com.haulmont.testtask.data.dao.MechanicDAO;
import com.haulmont.testtask.data.dao.OrderDAO;
import com.haulmont.testtask.data.entity.Client;
import com.haulmont.testtask.data.entity.Mechanic;
import com.haulmont.testtask.data.entity.Order;
import com.haulmont.testtask.data.entity.Status;
import com.haulmont.testtask.view.window.ClientWindow;
import com.haulmont.testtask.view.window.MechanicStatWindow;
import com.haulmont.testtask.view.window.MechanicWindow;
import com.haulmont.testtask.view.window.OrderWindow;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.themes.ValoTheme;

import java.util.*;

@Theme(value = ValoTheme.THEME_NAME)
public class MainUI extends UI {
    private HorizontalLayout mainLayout = new HorizontalLayout();

    private Button buttonClient = new Button("Clients");
    private Button buttonMechanic = new Button("Mechanics");
    private Button buttonOrder = new Button("Orders");
    private String leftButtonWidth = "200px";
    private String leftMenuWidth = "230px";

    private Button buttonAdd = new Button("Add new object");
    private Button buttonChange = new Button("Change selected");
    private Button buttonDelete = new Button("Delete selected");

    private Label label = new Label();
    private Grid grid = new Grid();
    private Panel filtersPanel = new Panel("Filters");
    private Button statButton = new Button();

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
        this.grid.setHeight("600px");

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

        setRightMenu(Client.class);
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
        setFiltersPanel(item);
        setStatButton(item);
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
        formatGrid(item);
    }

    private void formatGrid(Class item) {
        if (item.equals(Client.class)) {
            this.grid.setColumnOrder("id", "firstName", "secondName", "patronymic", "number");
        } else if (item.equals(Mechanic.class)) {
            this.grid.setColumnOrder("id", "firstName", "secondName", "patronymic", "taxes");
        } else if (item.equals(Order.class)) {
            this.grid.setColumnOrder("id", "description", "client", "mechanic",
                    "startDate", "endDate", "status", "price");

            this.grid.getColumn("endDate").setRenderer(new DateRenderer("%1$td/%1$tm/%1$tY"));
            this.grid.getColumn("startDate").setRenderer(new DateRenderer("%1$td/%1$tm/%1$tY"));
        }
    }

    private void setFiltersPanel(Class item) {
        if (item.equals(Order.class)) {
            String WIDTH_COMPONENT = "250px";

            NativeSelect clientSelect = new NativeSelect("Select client");
            clientSelect.setNullSelectionAllowed(true);
            clientSelect.setContainerDataSource(new BeanItemContainer<>(Client.class, this.clientDAO.getAll()));
            clientSelect.setWidth(WIDTH_COMPONENT);

            NativeSelect statusSelect = new NativeSelect("Select status");
            statusSelect.setNullSelectionAllowed(true);
            statusSelect.setContainerDataSource(new BeanItemContainer<>(Status.class, Arrays.asList(Status.values())));
            statusSelect.setWidth(WIDTH_COMPONENT);

            TextField descriptionInput = new TextField("Input description");
            descriptionInput.setWidth(WIDTH_COMPONENT);

            Button filterButton = new Button("Filter");
            filterButton.setWidth(WIDTH_COMPONENT);
            filterButton.addClickListener((Button.ClickListener) clickEvent -> {
                Map<String, Object> filterMap = new HashMap<>();
                filterMap.put("client", clientSelect.getValue());
                filterMap.put("status", statusSelect.getValue());
                filterMap.put("description", descriptionInput.getValue());

                this.grid.removeAllColumns();
                this.grid.setContainerDataSource(
                        new BeanItemContainer<>(Order.class,
                                this.orderDAO.findByDescriptionAndStatusAndClient(filterMap)));
            });

            Button removeFilters = new Button("Remove filters");
            removeFilters.setWidth(WIDTH_COMPONENT);
            removeFilters.addClickListener((Button.ClickListener) clickEvent -> {
                clientSelect.select(clientSelect.getNullSelectionItemId());
                statusSelect.select(statusSelect.getNullSelectionItemId());
                descriptionInput.clear();
                setGrid(Order.class);
            });

            VerticalLayout panelLayout = new VerticalLayout();
            panelLayout.setSpacing(true);
            panelLayout.setMargin(true);
            panelLayout.addComponent(clientSelect);
            panelLayout.addComponent(statusSelect);
            panelLayout.addComponent(descriptionInput);
            panelLayout.addComponent(filterButton);
            panelLayout.addComponent(removeFilters);

            this.filtersPanel.setContent(panelLayout);
            this.mainLayout.addComponent(this.filtersPanel);
        } else {
            this.mainLayout.removeComponent(this.filtersPanel);
        }
    }

    private void setStatButton(Class item) {
        this.statButton.getListeners(Button.ClickEvent.class)
                .forEach(listener -> this.statButton.removeListener(Button.ClickEvent.class, listener));

        if (item.equals(Mechanic.class)) {
            this.statButton.addClickListener((Button.ClickListener) clickEvent ->
                    addWindow(new MechanicStatWindow(this.orderDAO, this.mechanicDAO)));
            this.statButton.setCaption("Mechanic statistic");
            this.mainLayout.addComponent(this.statButton);
        } else {
            this.mainLayout.removeComponent(this.statButton);
        }
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