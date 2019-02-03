package com.haulmont.testtask.data.dao.daoimpl;

import com.haulmont.testtask.data.dao.OrderDAO;
import com.haulmont.testtask.data.entity.Order;

import java.util.Collection;
import java.util.Map;

public class OrderDAOImpl extends Transactional<Order> implements OrderDAO {
    @Override
    public void insert(Order order) {
        String message = "Merge order " + order;

        this.transaction((x -> {
            this.entityManager.merge(x);
            LOGGER.info(message);
        }), order, message);
    }

    @Override
    public void delete(Order order) {
        String message = "Delete order " + order;

        this.transaction((x -> {
            Order removeMechanic = entityManager.find(Order.class, x);
            if (removeMechanic != null)
                entityManager.remove(removeMechanic);
            LOGGER.info(message);
        }), order, message);
    }

    @Override
    public Order findByID(Long id) {
        String message = "Find order by id: " + id;

        return this.transaction(() -> {
            Order result = entityManager.createNamedQuery("Order.findById", Order.class)
                    .setParameter("id", id).getSingleResult();
            LOGGER.info(message);
            return result;
        }, message);
    }

    @Override
    public void update(Order order) {
        String message = "Update order: " + order;

        this.transaction((x -> {
            entityManager.merge(x);
            LOGGER.info(message);
        }), order, message);
    }

    @Override
    public void saveOrUpdate(Order order) {
        String message = "Save or update order: " + order;

        this.transaction((x -> {
            if (x.getId() == null)
                entityManager.persist(x);
            else
                entityManager.merge(x);
            LOGGER.info(message);
        }), order, message);
    }

    @Override
    public Collection<Order> getAll() {
        String message = "Find all order";

        return this.transaction(() -> {
            Collection<Order> result =
                    entityManager.createNamedQuery("Order.findAll", Order.class).getResultList();
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
