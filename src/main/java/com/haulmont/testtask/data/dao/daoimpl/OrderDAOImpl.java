package com.haulmont.testtask.data.dao.daoimpl;

import com.haulmont.testtask.data.dao.OrderDAO;
import com.haulmont.testtask.data.entity.Mechanic;
import com.haulmont.testtask.data.entity.Order;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.util.Collection;
import java.util.List;
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

    @Override
    public Collection<Order> findByDescriptionAndStatusAndClient(Map<String, Object> criteria) {
        StringBuilder message = new StringBuilder("Find orders by: ");
        criteria.forEach((key, value) -> message.append(key).append(" = ").append(value).append(" "));

        return this.transactional.transaction(() -> {
            DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Order.class);

            if (criteria.get("description") != null) {
                detachedCriteria.add(Restrictions.like("description", (String) criteria.get("description"),
                        MatchMode.START).ignoreCase());
            }

            if (criteria.get("status") != null) {
                detachedCriteria.add(Restrictions.eq("status", criteria.get("status")));
            }

            if (criteria.get("client") != null) {
                detachedCriteria.add(Restrictions.eq("client", criteria.get("client")));
            }

            Session session = this.transactional.getEntityManager().unwrap(Session.class);
            List result = detachedCriteria.getExecutableCriteria(session).list();
            LOGGER.info(message);
            return result;
        }, message.toString());
    }

    @Override
    public Collection<Order> findByMechanic(Mechanic mechanic) {
        String message = "Find orders by mechanic: " + mechanic;

        return this.transactional.transaction(() -> {
            Collection<Order> result = this.transactional.getEntityManager()
                    .createNamedQuery("Order.findByMechanic", Order.class).setParameter("mechanic", mechanic)
                    .getResultList();
            LOGGER.info(message);
            return result;
        }, message);
    }
}
