package com.haulmont.testtask.data.dao.daoimpl;

import com.haulmont.testtask.data.dao.OrderDAO;
import com.haulmont.testtask.data.entity.Order;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Map;

public class OrderDAOImpl implements OrderDAO {
    private Transactional<Order> transactional;
    private static final Logger LOGGER = LogManager.getLogger(OrderDAOImpl.class);

    public OrderDAOImpl() {
        this.transactional = new Transactional<>(LOGGER);
    }

    @Override
    public void insert(Order order) {
        String message = "Merge order " + order;

        this.transactional.transaction((x -> {
            this.transactional.getEntityManager().merge(x);
            LOGGER.info(message);
        }), order, message);
    }

    @Override
    public void delete(Order order) {
        String message = "Delete order " + order;

        this.transactional.transaction((x -> {
            Order removeMechanic = this.transactional.getEntityManager().find(Order.class, x.getId());
            if (removeMechanic != null)
                this.transactional.getEntityManager().remove(removeMechanic);
            LOGGER.info(message);
        }), order, message);
    }

    @Override
    public Order findByID(Long id) {
        String message = "Find order by id: " + id;

        return this.transactional.transaction(() -> {
            Order result = this.transactional.getEntityManager()
                    .createNamedQuery("Order.findById", Order.class)
                    .setParameter("id", id).getSingleResult();
            LOGGER.info(message);
            return result;
        }, message);
    }

    @Override
    public void update(Order order) {
        String message = "Update order: " + order;

        this.transactional.transaction((x -> {
            this.transactional.getEntityManager().merge(x);
            LOGGER.info(message);
        }), order, message);
    }

    @Override
    public void saveOrUpdate(Order order) {
        String message = "Save or update order: " + order;

        this.transactional.transaction((x -> {
            if (x.getId() == null)
                this.transactional.getEntityManager().persist(x);
            else
                this.transactional.getEntityManager().merge(x);
            LOGGER.info(message);
        }), order, message);
    }

    @Override
    public Collection<Order> getAll() {
        String message = "Find all order";

        return this.transactional.transaction(() -> {
            Collection<Order> result =
                    this.transactional.getEntityManager()
                            .createNamedQuery("Order.findAll", Order.class).getResultList();
            LOGGER.info(message);
            return result;
        }, message);
    }

    //TODO сделать запрос по нескольким критериям
    @Override
    public Collection<Order> findByDescriptionAndStatusAndClient(Map<String, Object> criteria) {
        return null;
    }
}
