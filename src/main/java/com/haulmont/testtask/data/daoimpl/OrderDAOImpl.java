package com.haulmont.testtask.data.daoimpl;

import com.haulmont.testtask.data.dao.OrderDAO;
import com.haulmont.testtask.data.entity.Order;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Collection;
import java.util.Map;

public class OrderDAOImpl implements OrderDAO {
    private EntityManagerFactory entityManagerFactory;

    public OrderDAOImpl() {
        this.entityManagerFactory = JPAUtil.getEntityManagerFactory();
    }

    @Override
    public void insert(Order order) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.persist(order);
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            System.err.println("Insert order: " + order + " is fail");
        } finally {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            entityManager.close();
        }
    }

    @Override
    public void delete(Order order) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.remove(order);
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            System.err.println("Delete order: " + order + " is fail");
        } finally {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            entityManager.close();
        }
    }

    @Override
    public Order findByID(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        Order result = null;
        try {
            entityTransaction.begin();
            result = entityManager.createNamedQuery("Order.findById", Order.class)
                    .setParameter("id", id).getSingleResult();
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            System.err.println("Find order by id: " + id + " is fail");
        } finally {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            entityManager.close();
        }
        return result;
    }

    @Override
    public void update(Order order) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.merge(order);
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            System.err.println("Update order: " + order + " is fail");
        } finally {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            entityManager.close();
        }
    }

    @Override
    public void saveOrUpdate(Order order) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            if (order.getId() == null)
                entityManager.persist(order);
            else
                entityManager.merge(order);
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            System.err.println("Save or update order: " + order + " is fail");
        } finally {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            entityManager.close();
        }
    }

    @Override
    public Collection<Order> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        Collection<Order> result = null;
        try {
            entityTransaction.begin();
            result = entityManager.createNamedQuery("Order.findAll", Order.class).getResultList();
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            System.err.println("Find all order is fail");
        } finally {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            entityManager.close();
        }
        return result;
    }

    //TODO сделать запрос по нескольким критериям
    @Override
    public Collection<Order> findByDescriptionAndStatusAndClient(Map<String, Object> criteria) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        Collection<Order> result = null;
        try {
            entityTransaction.begin();
            result = entityManager.createNamedQuery("Order.findAll", Order.class).getResultList();
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            System.err.println("Find all order is fail");
        } finally {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            entityManager.close();
        }
        return result;
    }
}
