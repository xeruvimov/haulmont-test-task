package com.haulmont.testtask.data.daoimpl;

import com.haulmont.testtask.data.dao.MechanicDAO;
import com.haulmont.testtask.data.entity.Mechanic;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Collection;

public class MechanicDAOImpl implements MechanicDAO {
    private EntityManagerFactory entityManagerFactory;

    public MechanicDAOImpl() {
        this.entityManagerFactory = JPAUtil.getEntityManagerFactory();
    }

    @Override
    public void insert(Mechanic mechanic) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.persist(mechanic);
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            System.err.println("Insert mechanic: " + mechanic + " is fail");
        } finally {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            entityManager.close();
        }
    }

    @Override
    public void delete(Mechanic mechanic) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.remove(mechanic);
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            System.err.println("Delete mechanic: " + mechanic + " is fail");
        } finally {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            entityManager.close();
        }
    }

    @Override
    public Mechanic findByID(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        Mechanic result = null;
        try {
            entityTransaction.begin();
            result = entityManager.createNamedQuery("Mechanic.findById", Mechanic.class)
                    .setParameter("id", id).getSingleResult();
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            System.err.println("Find mechanic by id: " + id + " is fail");
        } finally {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            entityManager.close();
        }
        return result;
    }

    @Override
    public void update(Mechanic mechanic) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.merge(mechanic);
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            System.err.println("Update mechanic: " + mechanic + " fail");
        } finally {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            entityManager.close();
        }
    }

    @Override
    public void saveOrUpdate(Mechanic mechanic) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            if (mechanic.getId() == null)
                entityManager.persist(mechanic);
            else
                entityManager.merge(mechanic);
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            System.err.println("Save or update mechanic: " + mechanic + " is fail");
        } finally {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            entityManager.close();
        }
    }

    @Override
    public Collection<Mechanic> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        Collection<Mechanic> result = null;
        try {
            entityTransaction.begin();
            result = entityManager.createNamedQuery("Mechanic.findAll", Mechanic.class).getResultList();
            entityManager.flush();
            entityTransaction.commit();
        } catch (Exception e) {
            System.err.println("Find all mechanic is fail");
        } finally {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            entityManager.close();
        }
        return result;
    }
}
