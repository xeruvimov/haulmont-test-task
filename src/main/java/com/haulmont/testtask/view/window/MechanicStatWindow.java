package com.haulmont.testtask.view.window;

import com.haulmont.testtask.data.dao.MechanicDAO;
import com.haulmont.testtask.data.dao.OrderDAO;
import com.haulmont.testtask.data.entity.Mechanic;
import com.haulmont.testtask.data.entity.Order;
import com.haulmont.testtask.data.entity.Status;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Grid;

import java.util.Collection;
import java.util.LinkedList;

public class MechanicStatWindow extends AbstractWindow {
    private OrderDAO orderDAO;

    private Collection<StatEntity> statEntityCollection;

    private Grid grid = new Grid();

    public MechanicStatWindow(OrderDAO orderDAO, MechanicDAO mechanicDAO) {
        this.orderDAO = orderDAO;
        this.statEntityCollection = new LinkedList<>();
        mechanicDAO.getAll().forEach(mechanic -> this.statEntityCollection.add(new StatEntity(mechanic)));
        initAllComponents();
    }

    private void initAllComponents() {
        configLayout();
        initCancelButton();
        this.buttons.removeComponent(this.okButton);

        this.grid.setContainerDataSource(new BeanItemContainer<>(StatEntity.class, this.statEntityCollection));
        this.grid.setWidth("650px");
        this.grid.setColumnOrder("mechanic", "doneOrder", "acceptedOrder", "plannedOrder");

        this.main.addComponent(this.grid);
        this.main.addComponent(this.buttons);
        setContent(this.main);
    }

    public class StatEntity {
        private Mechanic mechanic;
        private Collection<Order> orders;
        private Integer plannedOrder = 0;
        private Integer acceptedOrder = 0;
        private Integer doneOrder = 0;

        public StatEntity(Mechanic mechanic) {
            this.mechanic = mechanic;
            this.orders = orderDAO.findByMechanic(this.mechanic);
            this.orders.forEach(order -> {
                if (order.getStatus().equals(Status.DONE)) {
                    this.doneOrder++;
                } else if (order.getStatus().equals(Status.ACCEPTED)) {
                    this.acceptedOrder++;
                } else if (order.getStatus().equals(Status.PLANNED)) {
                    this.plannedOrder++;
                }
            });
        }

        public Mechanic getMechanic() {
            return mechanic;
        }

        public Integer getPlannedOrder() {
            return plannedOrder;
        }

        public Integer getAcceptedOrder() {
            return acceptedOrder;
        }

        public Integer getDoneOrder() {
            return doneOrder;
        }
    }
}
